// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const festivalId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getFestivalEditPage();
});

function getFestivalEditPage() {
    $.ajax({
        url: `/api/festivals/${festivalId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
            <div style="margin-left: 30px">
        <label for="titleInput">페스티벌 이름</label>
        <input type="text" id="titleInput" name="titleInput" required="" value="${data.title}"><br><br>

        <div style="display: grid; grid-template-columns: repeat(2, 1fr); width: 100%">
            <div style="width: 90%">
                <label for="openDateInput">시작 일시</label>
                <input type="datetime-local" id="openDateInput" name="openDateInput" required="" value="${data.openDate}"><br><br>
            </div>

            <div style="width: 90%">
                <label for="endDateInput">종료 일시</label>
                <input type="datetime-local" id="endDateInput" name="endDateInput" required="" value="${data.endDate}"><br><br>
            </div>
        </div>

        <label for="placeInput">페스티벌 장소</label>
        <input type="text" id="placeInput" name="placeInput" required="" value="${data.place}"><br><br>

        <label for="contentInput">페스티벌 설명</label>
        <textarea id="contentInput" name="contentInput" required="" ></textarea><br><br>

        <label for="imageInput">페스티벌 이미지</label>
        <input type="file" id="imageInput" name="imageInput" accept="image/*" onchange="previewImage(this)"><br>
        <img id="preview-image" width="200" alt="" src="${data.files[0].uploadFileUrl}"><br><br>

        <div style="display: grid; grid-template-columns: repeat(2, 1fr); width: 100%">
            <div style="width: 90%">
                <label for="reservationOpenDateInput">예매 오픈 일시</label>
                <input type="datetime-local" id="reservationOpenDateInput" name="reservationOpenDateInput"
                       required="" value="${data.reservationOpenDate}"><br><br>
            </div>

            <div style="width: 90%">
                <label for="reservationPlaceInput">예매처</label>
                <input type="text" id="reservationPlaceInput" name="reservationPlaceInput" required="" value="${data.reservationPlace}"><br><br>
            </div>
        </div>
        <label for="officialLinkInput">페스티벌 웹사이트/링크</label>
        <input type="url" id="officialLinkInput" name="officialLinkInput" required="" value="${data.officialLink}"><br><br>
        <label for="addTagInput">태그</label>
        <div id="tags-input-container">
            <div id="current-tags-input-container"></div>
            <a id="addTagInput" onclick="addTagInput()" style="color: #3b5998">추가</a>
        </div>
        <br><br>
        <div style="float: right;">
        <input type="submit" onclick="redirectFestivalMap()" value="페스티벌 맵 돌아가기" style="background-color: crimson">
        <input type="submit" id="festival-edit-submit" name="festival-edit-submit" onclick="editFestivalPost(${data.id})"
               value="페스티벌 정보 수정">
    </div>
    </div>
                `;
            $('#festival-edit-form').html(html);
            const contentTextarea = document.querySelector('#contentInput');
            contentTextarea.textContent = data.content;
            // HTML에서 추가할 컨테이너 요소를 가져옵니다.
            const tagContainer = document.getElementById('current-tags-input-container');

            // data.tags에 있는 각 태그를 반복합니다.
            data.tags.forEach(function (tag) {
                // input 요소를 동적으로 생성합니다.
                const input = document.createElement('input');
                input.type = 'text';
                input.value = tag.title; // data.tags에 있는 값을 input의 값으로 설정
                input.style = 'width: 90px; margin-right: 5px';

                // 삭제 버튼을 생성합니다.
                const removeButton = document.createElement('button');
                removeButton.textContent = 'x';

                // 삭제 버튼을 클릭할 때 해당 input 요소와 삭제 버튼을 제거합니다.
                removeButton.addEventListener('click', function () {
                    tagContainer.removeChild(input);
                    tagContainer.removeChild(removeButton);
                });

                // input과 삭제 버튼을 컨테이너에 추가합니다.
                tagContainer.appendChild(input);
                tagContainer.appendChild(removeButton);
            });
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

// 이미지 파일 미리보기
// 파일 선택 시 이벤트 리스너 추가
function previewImage(input) {
    const preview = document.getElementById('preview-image');

    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            // 미리보기 이미지 업데이트
            preview.src = e.target.result;
        };

        reader.readAsDataURL(input.files[0]);
    }
}

let tagInputCount = 0;

function addTagInput() {
    const container = document.getElementById('tags-input-container'); // 인풋 필드를 포함할 컨테이너를 선택

    // 새로운 인풋 필드를 생성합니다.
    const input = document.createElement('input');
    input.type = 'text';
    input.className = 'input-field';
    input.placeholder = 'Tags';
    input.style = 'width: 90px; margin-right: 5px';

    // 인풋 필드에 고유한 ID를 할당하여 추적합니다.
    input.id = `tagInput${tagInputCount}`;
    tagInputCount++;

    // 삭제 버튼을 생성합니다.
    const removeButton = document.createElement('button');
    removeButton.textContent = 'x';

    // 삭제 버튼을 클릭하면 해당 input 요소를 제거합니다.
    removeButton.addEventListener('click', function () {
        container.removeChild(input); // input 요소 제거
        container.removeChild(removeButton); // 삭제 버튼 제거
    });

    // 컨테이너에 인풋 필드와 삭제 버튼을 추가합니다.
    container.insertBefore(input, document.getElementById('addTagInput')); // 추가 버튼 위에 삽입
    container.insertBefore(removeButton, document.getElementById('addTagInput')); // 삭제 버튼도 추가 버튼 위에 삽입
}


function redirectFestivalMap() {
    window.location.href = `/api/festivals-map`;
}

// 페스티벌 수정
function editFestivalPost(festivalId) {
    // <form> 요소에서 데이터 가져오기
    const title = $('#titleInput').val();
    const place = $('#placeInput').val();
    const content = $('#contentInput').val();
    const openDate = $('#openDateInput').val();
    const endDate = $('#endDateInput').val();
    const reservationOpenDate = $('#reservationOpenDateInput').val();
    const reservationPlace = $('#reservationPlaceInput').val();
    const officialLink = $('#officialLinkInput').val();

    // 태그 값을 수집
    const tagInputs = document.querySelectorAll('#tags-input-container input[type="text"]');
    const tags = [];
    tagInputs.forEach(input => {
        const tagValue = input.value.trim();
        if (tagValue) {
            tags.push({title: tagValue});
        }
    });

    // 이미지 파일을 수집
    const imageFileInput = document.querySelector('#imageInput');
    const imageFile = imageFileInput.files[0];

    // 이미지 파일을 form-data에 추가
    const formData = new FormData();
    if (imageFile) {
        formData.append('files', imageFile);
    }

    // DTO 객체 생성
    const requestDto = {
        title: title,
        place: place,
        content: content,
        openDate: openDate,
        endDate: endDate,
        reservationOpenDate: reservationOpenDate,
        reservationPlace: reservationPlace,
        officialLink: officialLink,
    };
    console.log(requestDto);

    // 태그 값이 있는 경우에만 Tags 속성을 추가
    if (tags.length > 0) {
        requestDto.tagList = tags;
    }

    // JSON 문자열을 Blob 객체로 변환하여 FormData에 추가
    const jsonRequestDto = JSON.stringify(requestDto);
    const jsonBlob = new Blob([jsonRequestDto], {type: 'application/json'});
    formData.append('requestDto', jsonBlob);

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/festivals/${festivalId}`,
        type: 'PUT',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            console.log(festivalId, '페스티벌 수정 완료!');
            alert('페스티벌 수정 완료!');
            window.location.href = `/api/festivals/${festivalId}/page`;
        },
        error: function (err) {
            alert(err.responseText.statusMessage);
            console.log('Error:', err);
        }
    });
}