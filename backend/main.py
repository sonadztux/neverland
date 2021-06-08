import os
import datetime
import numpy as np
import cv2
import imutils
from google.cloud import storage
from keras.models import model_from_json
from flask import jsonify

# Bucket name store in environment variable
STORAGE_BUCKET = os.environ.get('STORAGE_BUCKET')

# Create cloud storage instance
storage_client = storage.Client()
bucket = storage_client.get_bucket(STORAGE_BUCKET)

IMG_SIZE = 200

# Global variables
model_json, model_weight, datetime_now = None, None, None


# Function to download image file from storage
# then store the file to tmp folder in cloud function runtime
def download_blob(bucket_name, source_blob_name, destination_file_name):
    destination_path = os.path.join('/tmp', destination_file_name)

    blob = bucket.blob(source_blob_name)
    blob.download_to_filename(destination_path)
    
    return destination_path


# Function to store uploaded image from request call
# to storage
def upload_to_storage(bucket_name, destination_path, destination_file_name, the_file):
    blob = bucket.blob(os.path.join(destination_path, destination_file_name))

    if destination_path == 'IMAGE_OUTPUT_DESTINATION_PATH': # CHANGE THIS
        blob.upload_from_filename(the_file)
        return blob.public_url
    else:
        blob.upload_from_string(
            the_file.read(),
            content_type=the_file.content_type
        )


# Function to load the model in storage to start 
# predicting the uploaded image
def predict_image(image):
    global model_json, model_weight

    if model_json is None and model_weight is None:
        model_json = download_blob(bucket_name=STORAGE_BUCKET, source_blob_name='MODEL_SOURCE_PATH/model.json', destination_file_name='model.json')
        model_weight = download_blob(bucket_name=STORAGE_BUCKET, source_blob_name='MODEL_SOURCE_PATH/model.h5', destination_file_name='model.h5')
    
    json_file = open(model_json,'r')
    loaded_model = json_file.read()
    json_file.close()

    load_model = model_from_json(loaded_model)
    load_model.load_weights(model_weight)

    img = cv2.imread(image)

    im = cv2.resize(img, (IMG_SIZE, IMG_SIZE))
    im = im.astype("float") / 255.0
    im = np.array(im).astype('float32')
    im = np.expand_dims(im, axis=0)

    (Fake,Real) = load_model.predict(im)[0]

    label = "Fake" if Fake > Real else "Real"
    proba = Fake if Fake > Real else Real
    label = "{}: {:.2f}%".format(label, proba * 100)

    img_output = imutils.resize(img, width=600)
    cv2.putText(img_output, label, (10, 25), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)
    cv2.imwrite('/tmp/output'+'.'+image.split('.')[-1], img_output)

    image_name = image.replace('input_', 'output_')
    img_output_url = upload_to_storage(bucket_name=STORAGE_BUCKET, destination_path='IMAGE_OUTPUT_DESTINATION_PATH', destination_file_name=image_name, the_file='/tmp/output'+'.'+image.split('.')[-1])

    message = {
        'status': 200,
        'message': 'OK',
        'result': label,
        'img_output_url': img_output_url
    }
    response = jsonify(message)
    response.status_code = 200

    return response


# Function to handle request call and catch the uploaded image
# from android app
def request_handler(request):
    global datetime_now
    datetime_now = datetime_now = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")

    uploaded_image = request.files.get('file')

    if not uploaded_image:
        return 'No image uploaded.', 400

    image_name = 'input_'+datetime_now+'.'+uploaded_image.filename.split('.')[-1]
    upload_to_storage(bucket_name=STORAGE_BUCKET, destination_path='INPUT_IMAGE_DESTINATION_PATH', destination_file_name=image_name, the_file=uploaded_image)

    image_tmp_path = download_blob(bucket_name=STORAGE_BUCKET, source_blob_name='INPUT_IMAGE_SOURCE_PATH'+image_name, destination_file_name=image_name)

    return predict_image(image_tmp_path)