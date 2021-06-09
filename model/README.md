# Photocuration
Predict manipulated images focused on human faces

## Description
Branch for Photocuration machine learning model.

## Documentation
1. Download dataset and put into your gdrive (when you running the model in collab) or put into your folder that same with your model
2. Split your data into training and validation data using sklearn
3. Create the training and validation batch using train generator
4. Doing the pre processing data using one hot encoding
5. Build the CNN Model
6. Train the model
7. Evaluate the model by plotting the model performance graph.
8. Save the model into .json and .h5


## Dataset
We combine Peter Wang et al. dataset with dataset that we collect. There is 3584 total face images, 2052 real face images and 1532 manipulated face images.
[DATASET &rarr;](https://drive.google.com/file/d/1yv_kM52isjpZgGm0AcAk-i426tMTpbGc/view?usp=sharing)

## Classes
There are 2 types of human face image to be classified measures on the eyes, nose, and mouth: Real or Fake.

## Google Colab
Please enjoy to copy and further develop the machine learning model [Google Colaboratory &rarr;](https://colab.research.google.com/drive/1LoScix83AMVgGek1dvTAICrJbhEtxJh5?usp=sharing)
