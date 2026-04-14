document.addEventListener("DOMContentLoaded", async () => {
  const textInput = document.getElementById("textInput");
  const result = document.getElementById("result");
  const translateBtn = document.getElementById("translateBtn");

  const data = await chrome.storage.local.get("selectedText");
  if (data.selectedText) {
    textInput.value = data.selectedText;
  }

  translateBtn.addEventListener("click", async () => {
    const text = textInput.value.trim();

    if (!text) {
      result.textContent = "Please enter text.";
      return;
    }

    result.textContent = "Translating...";
    result.classList.add("loading");

    try {
      const username = "translator";
      const password = "translator123";
      const auth = btoa(`${username}:${password}`);

      const response = await fetch("http://localhost:8080/translator_project_war/api/translate", {
        method: "POST",
        headers: {
          "Content-Type": "text/plain; charset=utf-8",
          "Authorization": `Basic ${auth}`
        },
        body: text
      });

      const data = await response.json();
      result.classList.remove("loading");
      result.textContent = data.translatedText || "No translation found.";
    } catch (error) {
      result.classList.remove("loading");
      result.textContent = "Error: " + error.message;
    }
  });
});