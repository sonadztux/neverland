# Photocuration
Predict manipulated images focused on human faces

## Project Cloud Architecture
<p align="center"><img src="https://cdn.discordapp.com/attachments/838946686533107803/852225172202455092/BangkitApp.png" width="600"></a></p>

## Description
We use two GCP services, Cloud Function and Cloud Storage, to implement end-to-end serverless machine learning deployment. 

The cloud function handle the HTTP request call from android app, store the uploaded image to storage, load the image and model, running prediction, and then return the prediction result. 

The cloud storage stores the ML model, input and output of the predicted image.

## Documentation
### Requirements
- Python <= 3.8
- Flask == 1.1.4
- GCP credit :P

more in [requirements.txt &rarr;](https://github.com/sonadztux/neverland/blob/master/backend/requirements.txt)

### Setup
1. Create new cloud storage bucket
2. Download model.json and model.h5 in model directory then upload them to storage bucket
3. Create new function in cloud function
4. Configure Runtime environment variables
`STORAGE_BUCKET` = `YOUR_STORAGE_BUCKET_NAME`
5. Set entry point to `request_handler` and then copy the code in main.py to main.py in the cloud
7. Edit this word in main.py
`MODEL_SOURCE_PATH`
`IMAGE_OUTPUT_DESTINATION_PATH`
`INPUT_IMAGE_DESTINATION_PATH`
6. Copy the requirements.txt as well
7. Deploy safely!