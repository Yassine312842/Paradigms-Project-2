# LLM-Powered Darija Translator RESTful Web Service

## Overview

This project is a Java-based RESTful web service that translates text into Moroccan Arabic Dialect (Darija) using a Large Language Model (LLM). The backend is built with Jakarta RESTful Web Services, Maven, Tomcat, and Jersey. The system also includes external clients that communicate with the API, such as a Python client and a Chrome extension.

The main goal of the project is to expose a secure translation endpoint that can be consumed by multiple client applications.

---

## Features

- RESTful translation endpoint
- Translation to Moroccan Darija using an LLM API
- Built with Jakarta RESTful Web Services
- Maven project structure
- Deployment on Apache Tomcat
- Basic Authentication protection for the API
- Python client
- Chrome extension using Manifest V3
- JSON-based response handling
- Error handling for API quota and temporary service issues

---

## Project Structure

```text
translator-project/
├── src/
│   └── main/
│       ├── java/
│       │   └── translator/
│       │       └── aui/
│       │           └── ma/
│       │               └── translatorproject/
│       │                   ├── TranslationService.java
│       │                   ├── TranslatorResource.java
│       │                   └── TranslationResponse.java
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml
├── python-client/
│   ├── python_client.py
│   └── requirements.txt
├── chrome-extension/
│   ├── manifest.json
│   ├── background.js
│   ├── popup.html
│   └── popup.js
├── pom.xml
└── README.md
```

---

## Technologies Used

### Backend
- Java
- Jakarta RESTful Web Services (JAX-RS)
- Jersey
- Maven
- Apache Tomcat
- Jackson

### Clients
- Python
- Chrome Extension (Manifest V3)

### External API
- LLM API for translation into Moroccan Darija

---

## API Endpoint

### Translate Text
**Endpoint:**
```http
POST /translator_project_war/api/translate
```

**Consumes:**
```text
text/plain
```

**Produces:**
```json
{
  "originalText": "When?",
  "translatedText": "إمتى؟"
}
```

---

## Authentication

The API is protected using **Basic Authentication** through Tomcat configuration.

Example credentials used during development:

- **Username:** `translator`
- **Password:** `translator123`

Authentication is configured in:
- `web.xml`
- `tomcat-users.xml`

---

## How to Run the Backend

### 1. Clone the repository
```bash
git clone <your-repository-link>
cd translator-project
```

### 2. Open the project in IntelliJ IDEA
Import it as a Maven project.

### 3. Configure Apache Tomcat
Add a Tomcat local server configuration in IntelliJ and deploy the generated WAR artifact.

### 4. Configure the API key
Add your LLM API key in Tomcat VM options, for example:

```text
-DOPENROUTER_API_KEY=your_api_key_here
```

or

```text
-DGEMINI_API_KEY=your_api_key_here
```

depending on the provider being used.

### 5. Start Tomcat
Run the project through the Tomcat configuration.

---

## Example API Test

### PowerShell
```powershell
$pair = "translator:translator123"
$encoded = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))

Invoke-RestMethod -Uri "http://localhost:8080/translator_project_war/api/translate" `
  -Method POST `
  -Headers @{ Authorization = "Basic $encoded" } `
  -ContentType "text/plain; charset=utf-8" `
  -Body "Where are you going?"
```

### Example Response
```json
{
  "originalText": "Where are you going?",
  "translatedText": "فين غادي؟"
}
```

---

## Python Client

The Python client sends authenticated requests to the REST API.

### Install dependency
```bash
pip install requests
```

### Run
```bash
python python_client.py
```

### Example
```text
Enter text to translate: Hello
Status code: 200
Response:
{"originalText":"Hello","translatedText":"سلام"}
```

---

## Chrome Extension

The Chrome extension allows the user to:
- select text on a webpage
- right-click and choose the translation action
- send the selected text to the backend API
- display the translated Darija result in the popup

### How to load the extension
1. Open Chrome
2. Go to `chrome://extensions/`
3. Enable **Developer mode**
4. Click **Load unpacked**
5. Select the `chrome-extension` folder

---

## Error Handling

The backend handles several cases such as:
- missing input text
- missing API key
- rate-limit errors
- temporary service unavailability
- invalid API responses

For example:
- if the API key is missing, the service returns a clear error message
- if the translation provider quota is exceeded, the response informs the user that the service is temporarily unavailable

---

## Current Status

### Completed
- RESTful backend setup
- Tomcat deployment
- Authentication
- Translation endpoint
- Python client
- Chrome extension prototype

### Remaining / To Improve
- PHP client
- React Native mobile app
- final side panel integration for the Chrome extension
- UML diagrams
- demo video
- optional technical post

---

## UML and Architecture

The architecture of the project is based on a client-server model:

- **Server:** Java RESTful web service deployed on Tomcat
- **Clients:** Python client, Chrome extension, PHP client, React Native app
- **External service:** LLM provider API used for Darija translation

The final submission will include:
- Class Diagram
- Deployment Diagram
- Optional Use Case / Sequence Diagram

---

## Challenges Faced

Some of the main challenges encountered during development included:
- configuring Jersey correctly on Tomcat
- handling deployment issues with JAX-RS
- managing authentication for external clients
- dealing with LLM API quota limits
- ensuring correct UTF-8 handling for Arabic output

---

## Future Improvements

- improve translation quality and prompt engineering
- add side panel support for the Chrome extension
- complete the PHP and mobile clients
- add better frontend styling
- store API keys more securely
- add logging and monitoring
- improve fallback behavior when quota is exceeded

---

## Author

Yassine Mallass

---

## Notes

This project was developed as part of a mini project on LLM-powered RESTful web services. It demonstrates how a secure Java REST API can be integrated with multiple clients and an external LLM service to provide translation into Moroccan Darija.
