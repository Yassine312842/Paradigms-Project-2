<?php
$result = "";
$error = "";
$text = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $text = $_POST["text"] ?? "";

    $url = "http://localhost:8080/translator_project_war/api/translate";
    $username = "translator";
    $password = "translator123";

    $ch = curl_init($url);

    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $text);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        "Content-Type: text/plain; charset=utf-8"
    ]);
    curl_setopt($ch, CURLOPT_USERPWD, $username . ":" . $password);
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

    if (curl_errno($ch)) {
        $error = "cURL Error: " . curl_error($ch);
    } elseif ($httpCode >= 200 && $httpCode < 300) {
        $decoded = json_decode($response, true);

        if (is_array($decoded) && isset($decoded["translatedText"])) {
            $result = $decoded["translatedText"];
        } else {
            $result = $response;
        }
    } else {
        $error = "HTTP Error $httpCode: " . $response;
    }

    curl_close($ch);
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Darija Translator PHP Client</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f5f5;
        }

        .container {
            max-width: 700px;
            margin: auto;
            background: white;
            padding: 24px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }

        h1 {
            margin-bottom: 20px;
        }

        textarea {
            width: 100%;
            height: 120px;
            padding: 12px;
            font-size: 16px;
            margin-bottom: 16px;
            resize: vertical;
        }

        button {
            padding: 10px 18px;
            font-size: 16px;
            cursor: pointer;
        }

        .result, .error {
            margin-top: 20px;
            padding: 15px;
            border-radius: 8px;
        }

        .result {
            background: #eaf7ea;
            border: 1px solid #b9dfb9;
        }

        .error {
            background: #fdeaea;
            border: 1px solid #e0b4b4;
            color: #a33;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Darija Translator PHP Client</h1>

    <form method="post">
        <label for="text">Enter text:</label><br><br>
        <textarea name="text" id="text" required><?= htmlspecialchars($text) ?></textarea><br>
        <button type="submit">Translate</button>
    </form>

    <?php if ($result): ?>
        <div class="result">
            <strong>Translation:</strong><br>
            <?= htmlspecialchars($result) ?>
        </div>
    <?php endif; ?>

    <?php if ($error): ?>
        <div class="error">
            <strong>Error:</strong><br>
            <?= htmlspecialchars($error) ?>
        </div>
    <?php endif; ?>
</div>
</body>
</html>