chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "translateToDarija",
    title: "Translate to Darija",
    contexts: ["selection"]
  });
});

chrome.contextMenus.onClicked.addListener((info) => {
  if (info.menuItemId === "translateToDarija" && info.selectionText) {
    chrome.storage.local.set({ selectedText: info.selectionText });
  }
});