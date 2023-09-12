// 페스티벌 작성
function submitFestivalPost() {
    // <form> 요소에서 데이터 가져오기
    const title = document.querySelector('#titleInput').value;
    const place = document.querySelector('#placeInput').value;
    const content = document.querySelector('#contentInput').value;
    const openDate = document.querySelector('#openDateInput').value;
    const endDate = document.querySelector('#endDateInput').value;
    const reservationOpenDate = document.querySelector('#reservationOpenDateInput').value;
    const reservationPlace = document.querySelector('#reservationPlaceInput').value;
    const officialLink = document.querySelector('#officialLinkInput').value;

// 태그 값을 수집
const tagInputs = document.querySelectorAll('#tags-input-container input[type="text"]');
const tags = [];
tagInputs.forEach(input => {
    const tagValue = input.value.trim();
    if (tagValue) {
        tags.push({ title: tagValue });
    }
});

// 이미지 파일을 수집
    const imageFileInput = document.querySelector('#imageInput');
    const imageFile = imageFileInput.files[0];

// 위도, 경도 정보
// URL에서 쿼리 문자열을 가져옵니다.
    var queryString = window.location.search;

// URLSearchParams를 사용하여 쿼리 문자열을 파싱합니다.
    var queryParams = new URLSearchParams(queryString);
    const latitude = queryParams.get('lat');
    const longitude = queryParams.get('lng');

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
        latitude: latitude,
        longitude: longitude,
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
        url: `/api/festivals`,
        type: 'POST',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            alert(data.statusMessage);
            // AJAX 요청이 완료되면 리다이렉트 수행
            window.location.href = '/api/users/festivals-map';
        },
        error: function (err) {
            alert(err.responseText.statusMessage);
            console.log('Error:', err);
        }
    });
}

// 이미지 파일 미리보기
// 파일 인풋 엘리먼트 가져오기
const fileInput = document.getElementById('imageInput');

// 이미지 미리보기를 위한 img 엘리먼트 가져오기
const previewImage = document.getElementById('preview-image');

// 파일 선택 시 이벤트 리스너 추가
fileInput.addEventListener('change', function () {
    // 선택한 파일 가져오기
    const file = fileInput.files[0];

    if (file) {
        // 선택한 파일이 이미지인지 확인
        if (file.type.startsWith('image/')) {
            // 이미지 파일인 경우, 미리보기를 위해 FileReader 사용
            const reader = new FileReader();

            reader.onload = function (e) {
                // 파일 불러오기가 완료되면 미리보기 이미지 업데이트
                previewImage.src = e.target.result;
            };

            // 파일을 읽어옵니다.
            reader.readAsDataURL(file);
        } else {
            alert('이미지 파일을 선택하세요.');
            fileInput.value = ''; // 파일 선택 취소
        }
    }
});

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
    window.location.href = `/api/users/festivals-map`;
}