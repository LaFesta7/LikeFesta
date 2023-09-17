// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const festivalId = pathSegments[pathSegments.length - 4];
const reviewId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getReviewInputs();
});

function getReviewInputs() {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}`;
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
                <div>
                    <label for="titleInput">제목:</label>
                    <input type="text" id="titleInput" name="titleInput" required="" value="${data.title}">
                </div>

                <div>
                    <label for="contentInput">내용:</label>
                    <textarea id="contentInput" name="contentInput" rows="4" required=""></textarea>
                </div>

                <div>
                    <label for="imageInput">첨부파일:</label>
                    <input type="file" id="imageInput" name="imageInput" onchange="showPreviewImage(this)">
                    <img id="preview-image" width="200" alt="" src="${data.fileUrl}"><br><br>
                </div>

                <div>
                    <button type="submit" onclick="editReview()">변경사항 저장</button>
                </div>
                `;
            $('#review-edit-form').html(html);
            const contentTextarea = document.querySelector('#contentInput');
            contentTextarea.textContent = data.content;
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function editReview() {
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
        url: `/api/festivals/${festivalId}/reviews/${reviewId}`,
        type: 'PUT',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            alert('리뷰 수정이 완료되었습니다!');
            window.location.href = `/api/festivals/${data.festivalId}/reviews/${data.id}/page`;
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

// 이미지 파일 미리보기
// 파일 선택 시 이벤트 리스너 추가
function showPreviewImage(input) {
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