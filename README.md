# LibraAI

An AI voice-emergency assistant! **Won 4th place at Hack 'N' Tackle hackathon.**

> *To train custom voice model, run the notebook at https://github.com/ashwin2k/wake-word-detection*

## What does it do?
- LibraAI uses a GRU based model top detect the pretrained wake word "help me" from the user. 
- It uses a persistent notification that actually serves 2 functions:
  1) A Quick Access Notification bar to access the help functions directly
  2) To actively listen for the trigger word even when the phone is in sleep mode or the app is closed.
- When the trigger word is detected:
  1) The flashlight is flashed in an SOS manner
  2) The emergency contacts are notified
  3) The police helpline is called.

## To run the app
Clone the project and build the apk using Android Studio!

## To see our hackathon pitch deck, click ![here](https://docs.google.com/presentation/d/1AtgSaQdJdMD_8jf7kf5ynEo-peDvuvPh/edit?usp=share_link&ouid=100827058064004915685&rtpof=true&sd=true)

## Demo Video:
![Demo1](https://drive.google.com/file/d/1TNwj7U_D7p0tSuSEAVEq5xn7j6MFzwyO/view?usp=share_link)
![Demo2](https://drive.google.com/file/d/1TnZqh-mE0QAiPKiL3zcJFYmTA_ToFHYq/view?usp=share_link)

## Neural Network Architecture
We use a GRU based architecture as shown here:

![image](https://user-images.githubusercontent.com/42934189/215972580-f9283004-8fad-482d-823c-80f9ae9fc7bb.png)

## Overall System Architecture
![image](https://user-images.githubusercontent.com/42934189/215972635-649b0d9f-db1b-4dad-8550-59a04e7f2a81.png)

## Screenshots
![image](https://user-images.githubusercontent.com/42934189/215970530-7c27e1e6-5f62-4e86-a56d-3387a7a5bc11.png)
![image](https://user-images.githubusercontent.com/42934189/215970569-440e1075-5f03-48be-b7e7-d42877351fb7.png)
![image](https://user-images.githubusercontent.com/42934189/215970592-3bd0895b-da4a-4847-9396-0a53366fa361.png)
![image](https://user-images.githubusercontent.com/42934189/215970604-91811484-c76d-4ed5-8fc0-d1b1f9abb870.png)
![image](https://user-images.githubusercontent.com/42934189/215970798-a60a49cf-e4d2-40f0-92d3-1e0a6dbc5e53.png)

Uses chaquopy as a dependancy. If voice recognition doesnt work, kindly check if the permissions are allowed in settings.
