import requests
from requests.auth import HTTPBasicAuth

url = "http://localhost:8080/translator_project_war/api/translate"
username = "translator"
password = "translator123"

text = input("Enter text to translate: ")

try:
    response = requests.post(
        url,
        data=text.encode("utf-8"),
        headers={"Content-Type": "text/plain; charset=utf-8"},
        auth=HTTPBasicAuth(username, password)
    )

    print("Status code:", response.status_code)
    print("Response:")
    print(response.text)

except Exception as e:
    print("Error:", e)