import cv2
import numpy as np
import pyrebase

from pyagender import PyAgender

agender = PyAgender()
capture = cv2.VideoCapture(0)

config = {
  "apiKey": "AIzaSyDT8zvCg7Ax3h_IYtpwaawKe_80LmoN0aY",
  "authDomain": "craeye-fa4f0.firebaseapp.com",
  "databaseURL": "https://craeye-fa4f0.firebaseio.com",
  "storageBucket": "craeye-fa4f0.appspot.com"
}
firebase = pyrebase.initialize_app(config)

fb_database = firebase.database()

while(True):
	ret, frame = capture.read()
	
	faces = agender.detect_genders_ages(frame)
	
	age_values = []
	gender_values = []
	input_data = {}
	
	if faces:
		for int_index in range(0, len(faces)):
			for value in faces[int_index]:
				if value == "age":
					if faces[int_index][value] <= 25:
						age_values.append("Cat-01")
					elif faces[int_index][value] <= 50:
						age_values.append("Cat-02")
					else:
						age_values.append("Cat-03")
				if value == "gender":
					if faces[int_index][value] <= 0.5:
						gender_values.append("Male")
					else:
						gender_values.append("Female")
			break
	
	input_data["age"] = str(age_values)
	input_data["gender"] = str(gender_values)
	input_data["category"] = "Electronics"
	print(input_data)
	cv2.imshow('frame', frame)
	
	fb_database.child('camera_01').set(input_data)
	
	if cv2.waitKey(1) & 0xFF == ord('q'):
		break

capture.release()
cv2.destroyAllWindows()
