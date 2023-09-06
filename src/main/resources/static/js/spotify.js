function search() {
    const keywordInput = document.getElementById("keywordInput");
    const keyword = keywordInput.value;

    if (!keyword) {
        console.error('검색어를 입력해주세요.');
        return;
    }

    getAccessToken()
        .then(accessToken => {
            const headers = {
                'Authorization': 'Bearer ' + accessToken,
                'Host': 'api.spotify.com',
                'Content-type': 'application/json'
            };

            const url = 'https://api.spotify.com/v1/search?type=track&market=KR&limit=3&q=' + encodeURIComponent(keyword);

            return fetch(url, {
                method: 'GET',
                headers: headers
            });
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const tracks = data.tracks.items;
            const tracksList = document.getElementById('tracksList');
            tracksList.innerHTML = '';

            for (let i = 0; i < tracks.length; i++) {
                const track = tracks[i];
                const artist = track.artists[0].name;
                const title = track.name;
                const previewUrl = track.preview_url;
                const imageUrl = track.album.images[0].url;

                // 결과를 표시하기 위한 HTML 요소 생성
                const listItem = document.createElement('li');
                const artistElement = document.createElement('div');
                const titleElement = document.createElement('div');
                const previewUrlElement = document.createElement('audio');
                const imageElement = document.createElement('img');

                artistElement.textContent = 'Artist: ' + artist;
                titleElement.textContent = 'Title: ' + title;

                if (previewUrl) {
                    previewUrlElement.src = previewUrl;
                    previewUrlElement.controls = true; // controls 속성 추가
                } else {
                    previewUrlElement.textContent = '프리뷰 없음';
                }

                imageElement.src = imageUrl;

                listItem.appendChild(artistElement);
                listItem.appendChild(titleElement);
                listItem.appendChild(previewUrlElement);
                listItem.appendChild(imageElement);

                tracksList.appendChild(listItem);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function getAccessToken() {
    const CLIENT_ID = 'd107c976d6eb4b33befbbec657b6baa9';
    const CLIENT_SECRET = '9f7c7d6524304b70955c3ec0ba9563ea';
    const authUrl = 'https://accounts.spotify.com/api/token';

    const params = new URLSearchParams();
    params.append('grant_type', 'client_credentials');
    params.append('client_id', CLIENT_ID);
    params.append('client_secret', CLIENT_SECRET);

    return fetch(authUrl, {
        method: 'POST',
        body: params
    })
        .then(response => response.json())
        .then(data => data.access_token)
        .catch(error => {
            console.error('Error:', error);
            return 'error';
        });
}

// 입력값을 JSON 형식으로 출력
// JSON 데이터를 서버로 전송하거나 필요한 작업을 수행할 수 있습니다.
document.getElementById("post-id-submit").addEventListener("click", function () {
    const form = document.getElementById("post-form");
    const formData = new FormData(form);
    const jsonObject = {};

    for (const pair of formData.entries()) {
        jsonObject[pair[0]] = pair[1];
    }

    const jsonData = JSON.stringify(jsonObject);
    console.log(jsonData);
});