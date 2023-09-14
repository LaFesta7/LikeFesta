// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const festivalId = pathSegments[pathSegments.length - 3];

function postReview() {
    // <form> 요소에서 데이터 가져오기
    const title = document.querySelector('#titleInput').value;
    const content = document.querySelector('#contentInput').value;

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
        content: content,
    };

    // JSON 문자열을 Blob 객체로 변환하여 FormData에 추가
    const jsonRequestDto = JSON.stringify(requestDto);
    const jsonBlob = new Blob([jsonRequestDto], {type: 'application/json'});
    formData.append('requestDto', jsonBlob);

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews`,
        type: 'POST',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            alert('리뷰 작성이 완료되었습니다!');
            window.location.href = `/api/festivals/${data.festivalId}/reviews/${data.id}/page`;
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
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