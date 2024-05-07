document.addEventListener('DOMContentLoaded', () => {
    const apiKey = 'c87216d61af858aa5ea5992ea98d30e7';
    const city = 'Seoul';
    const url = `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}&units=metric&lang=kr`;

    const darkWeatherConditions = ["04n","04d", "50n", "13n", "11n", "10n", "09n"];

    const weatherBackgrounds = {
        "01d": "../../img/weather/sunny-day.png", // 맑은(낮)
        "01n": "../../img/weather/clear-night.png", // 맑은(밤)
        "02d": "../../img/weather/partly-cloudy-day.png", // 약간 흐린(낮)
        "02n": "../../img/weather/partly-cloudy-night.png", // 약간 흐린(밤)
        "03d": "../../img/weather/cloudy-day.png", // 흐린(낮)
        "03n": "../../img/weather/cloudy-night.png", // 흐린(밤)
        "04n": "../../img/weather/broken-clouds-day.png", // 아주 흐린(낮)
        "04d": "../../img/weather/broken-clouds-night.png", // 아주 흐린(밤)
        "09d": "../../img/weather/shower-rain-day.png", // 소나기(낮)
        "09n": "../../img/weather/shower-rain-night.png", // 소나기(밤)
        "10d": "../../img/weather/rain-day.png", //비(낮)
        "10n": "../../img/weather/rain-night.png", // 비(밤)
        "11d": "../../img/weather/thunderstorm-day.png", // 천둥 번개(낮)
        "11n": "../../img/weather/thunderstorm-night.png", // 천둥 번개(밤)
        "13d": "../../img/weather/snow-day.png", // 눈(낮)
        "13n": "../../img/weather/snow-night.png", // 눈(밤)
        "50d": "../../img/weather/mist-day.png", // 안개(낮)
        "50n": "../../img/weather/mist-night.png" // 안개(밤)
    };

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const temperature = `${data.main.temp.toFixed(1)}°C`;
            const weatherDescription = data.weather[0].description;
            const weatherIconCode = data.weather[0].icon; // 아이콘 코드
            const weatherIconUrl = `http://openweathermap.org/img/wn/${weatherIconCode}@2x.png`;
            const time = new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
            const date = new Date().toLocaleDateString('ko-KR');

            document.getElementById('temperature').textContent = temperature;
            document.getElementById('weatherIcon').src = weatherIconUrl;
            document.getElementById('weatherDescription').textContent = weatherDescription;
            document.getElementById('weatherTime').textContent = time;
            document.getElementById('currentDate').textContent = date;
            document.getElementById('location').textContent = city;

            //아이콘에 따른 배경 이미지 설정
            const backgroundUrl = weatherBackgrounds[weatherIconCode] || '../../img/weather/sunny-day.png';
            document.querySelector('.weather-widget').style.backgroundImage = `url('${backgroundUrl}')`;

            // 특정 날씨 아이콘에 따라 클래스를 추가
            if (darkWeatherConditions.includes(weatherIconCode)) {
                document.querySelector('.weather-widget').classList.add('dark-text');
            }
        })
        .catch(error => console.error('Error fetching weather data:', error));
});
