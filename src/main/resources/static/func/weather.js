const API_KEY = "10e91e020c8b2bfc26ee4e1dd245936a";

function onGeoOk(position) {
    const lat = position.coords.latitude;
    const lon = position.coords.longitude;
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const weather = document.querySelector("#weather p:first-child");
            const city = document.querySelector("#weather p:last-child");
        city.innerText = `${data.name}`;
        weather.innerText = `${data.weather[0].main} / ${data.main.temp}℃ @`;
    });
}
function onGeoErr() {
    alert("사용자의 위치를 파악할 수 없습니다.");
}

navigator.geolocation.getCurrentPosition(onGeoOk, onGeoErr);
