<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Renee's Blog | Meme Maker</title>
    <link rel="stylesheet" href="css/meme.css">
</head>
<body>
<div class="color-options">
    <input type="color" id="color">
    <div
            class="color-option"
            style="background-color:#ff6289"
            data-color="#ff6289">
    </div>
    <div
            class="color-option"
            style="background-color:#f7a3c2"
            data-color="#f7a3c2">
    </div>
    <div class="color-option"
         style="background-color:#32a287"
         data-color="#32a287">
    </div>
    <div
            class="color-option"
            style="background-color:#e3d7b8"
            data-color="#e3d7b8">
    </div>
    <div
            class="color-option"
            style="background-color:#5486c5"
            data-color="#5486c5">
    </div>
    <div
            class="color-option"
            style="background-color:#414073"
            data-color="#414073">
    </div>
    <div
            class="color-option"
            style="background-color:#a573b8"
            data-color="#a573b8">
    </div>
    <div
            class="color-option"
            style="background-color:#ee4035"
            data-color="#ee4035">
    </div>
    <div
            class="color-option"
            style="background-color:#f37736"
            data-color="#f37736">
    </div><div
        class="color-option"
        style="background-color:#fdf498"
        data-color="#fdf498">
</div><div
        class="color-option"
        style="background-color:#7bc043"
        data-color="#7bc043">
</div><div
        class="color-option"
        style="background-color:#0392cf"
        data-color="#0392cf">
</div>
</div>
<canvas></canvas>
<div class="btns">
    <input id="line-width" type="range" min="1" max="20" value="10" step="0.1"/>
    <button id="mode-btn">💧Fill</button>
    <button id="destroy-btn">💣 Destroy</button>
    <button id="eraser-btn">❌ Erase</button>
    <label for="file">
        ➕ Add Photo
        <input type="file" accept="image/*" id="file" />
    </label>
    <input type="text" placeholder="✏️ Add Text here..." id="text"/>
    <button id="save">🖼 Save Image</button>
    <button id="main"><a href="main">🔙 Go to Main</a></button>
</div>
<script src="func/meme.js"></script>
</body>
</html>
