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

document.getElementById("post-id-submit").addEventListener("click", function () {
    const selectedTracks = getSelectedTracks(); // 선택한 곡 정보를 가져오는 함수를 호출하세요.

    // 선택한 곡이 없는지 확인
    if (selectedTracks.length === 0) {
        console.error('선택한 곡이 없습니다.');
        return;
    }

    const formData = new FormData();
    formData.append('selectedTracks', JSON.stringify(selectedTracks));

    const festivalId = 123; // 실제 페스티벌 ID로 대체하세요

    fetch(`/festivals/${festivalId}/lineups`, {
        method: 'POST',
        body: formData,
        headers: {
            'Accept': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('라인업이 저장되었습니다:', data);
        })
        .catch(error => {
            console.error('라인업 저장 중 오류 발생:', error);
        });
});

function getSelectedTracks() {
    // 선택한 곡을 수집하는 함수를 구현하세요.
    // "selectedTracksInput" 필드 또는 다른 방법을 사용하여 선택한 곡을 수집할 수 있습니다.
    // 선택한 곡 객체의 배열을 반환하세요.
    // 예:
    const selectedTracks = [
        {
            artist: '아티스트 1',
            title: '노래 1',
            // 필요한 다른 곡 속성을 추가하세요.
        },
        {
            artist: '아티스트 2',
            title: '노래 2',
            // 필요한 다른 곡 속성을 추가하세요.
        },
        // 필요한 만큼 선택한 곡을 추가하세요.
    ];

    return selectedTracks;
}
