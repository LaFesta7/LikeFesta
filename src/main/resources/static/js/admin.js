$(document).ready(function () {
    // 쿠키에서 'Authorization' 토큰을 가져옵니다.
    const token = Cookies.get('Authorization');
    // 토큰이 존재하면 서버 API를 호출하여 유효성을 검사합니다.
    if (token === undefined) {
        // 토큰이 존재하지 않으면, 사용자는 로그인 상태가 아닙니다.
        $('#logoutForm').hide();  // 로그아웃 버튼을 숨깁니다
        $('#loginButton').show();  // 로그인 버튼을 표시합니다
    } else {
        $('#logoutForm').show();  // 로그아웃 버튼을 표시합니다
        $('#loginButton').hide();  // 로그인 버튼을 숨깁니다
        let firstPage = 0;
        getOrganizerRequests(firstPage);
        getFestivalRequests(firstPage);
        getUsers(firstPage);
        getTags(firstPage);
        getBadges(firstPage);
    }
});

// 주최자 승인 요청
function getOrganizerRequests(pageNum) {
    $.ajax({
        url: `/api/admin/users/organizer-requests?page=${pageNum}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            data.content.forEach(function (organizer){ // Loop through each festival
                html += `<tr>
                    <td>${organizer.id}</td>
                    <td>${organizer.username}</td>
                    <td>${organizer.nickname}</td>
                    <td>${organizer.email}</td>
                    <td><a id="checkbox${organizer.id}" onclick="alertOrganizerRequest('${organizer.username}', '${organizer.id}')">승인</a></td>
                </tr>`;
            });
            ;
            $('#organizer-request-table-body').html(html);

            makeOrganizerRequestPagination(data)

        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

//페이지네이션
function makeOrganizerRequestPagination(page){
    let pagination = $("#organizer-request-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<button><a onclick='getOrganizerRequests(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="getOrganizerRequests(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='getOrganizerRequests(${cur + 1})'>다음</a></button>`);
    }
}

function alertOrganizerRequest(username, userId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + username + "'의 주최자 권한을 승인하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        approveOrganizerRequest(userId);
    }
}

function approveOrganizerRequest(userId) {
    $.ajax({
        url: `/api/admin/users/organizer-requests/${userId}`,
        type: 'PATCH',
        success: function (data) {
            alert(data.username + '의 주최자 승인이 완료되었습니다!');
            getOrganizerRequests();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}

// 페스티벌 승인 요청
function getFestivalRequests(pageNum) {
    $.ajax({
        url: `/api/admin/festival-requests?page=${pageNum}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            data.content.forEach(function (fesRq) { // Loop through each festival
                html += `<tr>
                        <td>${fesRq.id}</td>
                        <td><a href="/api/festival-requests/${fesRq.id}" target="_blank">${fesRq.title}</a></td>
                        <td>${fesRq.place}</td>
                        <td>${fesRq.content}</td>
                        <td>${fesRq.openDate} ~ ${fesRq.endDate}</td>
                        <td><a href="${fesRq.officialLink}" target="_blank">Official Link</a></td>
                        <td>${fesRq.nickname}</td>
                        <td><input type="checkbox" onclick="toggleCheckbox(${fesRq.id})"></td>
                    </tr>`;
            })
            ;
            $('#festival-request-table-body').html(html);
            makeFestivalRequestPagination(data);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

//페이지네이션
function makeFestivalRequestPagination(page){
    let pagination = $("#festival-request-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<button><a onclick='getFestivalRequests(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="getFestivalRequests(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='getFestivalRequests(${cur + 1})'>다음</a></button>`);
    }
}

// 선택된 항목을 저장하기 위한 배열을 생성합니다.
const selectedItems = [];

// 체크 박스가 클릭될 때 호출되는 함수입니다.
function toggleCheckbox(itemId) {
    const index = selectedItems.indexOf(itemId);
    if (index === -1) {
        // 배열에 추가합니다.
        selectedItems.push(itemId);
    } else {
        // 배열에서 제거합니다.
        selectedItems.splice(index, 1);
    }
}

function batchApprovalProcess() {
    const confirmation = window.confirm('선택된 항목을 승인하시겠습니까?');

    if (confirmation) {
        const successfulApprovals = [];
        const errorApprovals = [];

        // 비동기 처리를 위한 Promise를 사용합니다.
        const approvalPromises = selectedItems.map(itemId => {
            return new Promise((resolve, reject) => {
                // approveFestivalRequest 함수가 비동기로 작동하도록 가정합니다.
                approveFestivalRequest(itemId)
                    .then(success => {
                        successfulApprovals.push(itemId);
                        resolve();
                    })
                    .catch(error => {
                        errorApprovals.push(itemId);
                        reject(error);
                    });
            });
        });

        Promise.all(approvalPromises)
            .then(() => {
                // 모든 작업이 성공한 경우
                if (successfulApprovals.length === selectedItems.length) {
                    alert('모든 항목이 성공적으로 승인되었습니다.');
                    getFestivalRequests();
                } else {
                    alert('일부 항목은 승인되었습니다. 다음 항목들은 승인되지 않았습니다: ' + errorApprovals.join(', '));
                    getFestivalRequests();
                }
            })
            .catch(error => {
                console.error('오류 발생:', error);
            });
    }
}

function approveFestivalRequest(festivalRequestId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/api/admin/festival-requests/${festivalRequestId}`,
            type: 'PATCH',
            success: function (data) {
                console.log(data.id + ' 승인 완료');
                resolve(data);
            },
            error: function (err) {
                console.log(err + ' 에러');
                reject(err);
            }
        });
    });
}

// 유저 강제 탈퇴
function getUsers(pageNum) {
    $.ajax({
        url: `/api/admin/users?page=${pageNum}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            data.content.forEach(function (user) { // Loop through each festival
                html += `<tr>
                    <td>${user.id}</td>
                    <td>${user.role}</td>
                    <td>${user.username}</td>
                    <td>${user.nickname}</td>
                    <td>${user.email}</td>
                    <td><a id="checkbox${user.id}" onclick="alertUserWithdrawal('${user.username}', '${user.id}')">탈퇴</a></td>
                </tr>`;
            })
            ;
            $('#users-table-body').html(html);
            makeUserPagination(data)
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

//페이지네이션
function makeUserPagination(page){
    let pagination = $("#user-management-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<button><a onclick='getUsers(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="getUsers(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='getUsers(${cur + 1})'>다음</a></button>`);
    }
}

function alertUserWithdrawal(username, userId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + username + "' 사용자를 탈퇴 처리하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        withdrawUser(userId);
    }
}

function withdrawUser(userId) {
    $.ajax({
        url: `/api/admin/users/${userId}/withdrawal`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            getUsers();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}

// 태그 관리
function getTags(pageNum) {
    $.ajax({
        url: `/api/tags?page=${pageNum}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            data.content.forEach(function (tag){ // Loop through each festival
                html += `<tr>
                    <td>${tag.id}</td>
                    <td id="tagName${tag.id}">${tag.title}</td>
                    <td><a onclick="showTagInput('${tag.title}', '${tag.id}')">수정</a></td>
                    <td><a onclick="alertTagDelete('${tag.title}', '${tag.id}')">삭제</a></td>
                </tr>`;
            })
            ;
            $('#tags-table-body').html(html);
            makeTagPagination(data);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

//페이지네이션
function makeTagPagination(page){
    let pagination = $("#tags-management-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<button><a onclick='getTags(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="getTags(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='getTags(${cur + 1})'>다음</a></button>`);
    }
}

function showTagInput(title, id) {
    // 해당 테이블 셀을 찾습니다.
    const cell = $(`#tagName${id}`);

    // 셀 내용을 현재 내용으로 저장합니다.
    const currentContent = cell.text();

    // 수정 버튼을 '확인'으로 변경합니다.
    cell.next().html('<a onclick="alertTagModify(\'' + currentContent + '\', ' + id + ')">확인</a>');

    // 삭제 버튼을 '취소'로 변경합니다.
    cell.next().next().html('<a onclick="getTags()">취소</a>');

    // input 요소를 생성하고 현재 내용으로 초기화합니다.
    const input = $('<input>', {
        type: 'text',
        id: `tagInput${id}`,
        value: currentContent
    });

    // 현재 셀을 비워주고 input 요소를 추가합니다.
    cell.empty().append(input);
}

function alertTagModify(tagName, tagId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + tagName + "' 태그를 수정하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        modifyTag(tagId);
    } else {
        getTags();
    }
}

function modifyTag(tagId) {
    const tagInput = document.getElementById(`tagInput${tagId}`);
    const requestDto = {
        title: tagInput.value
    };

    $.ajax({
        url: `/api/tags/${tagId}`,
        type: 'PATCH',
        data: JSON.stringify(requestDto), // 데이터 객체를 JSON 문자열로 변환하여 전송
        contentType: 'application/json',
        success: function (data) {
            alert(data.title + '로 태그를 수정하였습니다.');
            getTags();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}

function alertTagDelete(tagName, tagId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + tagName + "' 태그를 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        deleteTag(tagId);
    }
}

function deleteTag(tagId) {
    $.ajax({
        url: `/api/tags/${tagId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            getTags();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}

// 뱃지 관리
function getBadges(pageNum) {
    $.ajax({
        url: `/api/admin/badges?page=${pageNum}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            data.content.forEach(function (badge) { // Loop through each festival
                if(badge.files == null){
                    html += `<tr>
                    <td>${badge.id}</td>
                    <td id="badgeImage${badge.id}"><img alt="Badge Image" width="100"></td>`
                }else{
                    html += `<tr>
                    <td>${badge.id}</td>
                    <td id="badgeImage${badge.id}"><img src="${badge.files[0].uploadFileUrl}" alt="Badge Image" width="100"></td>`
                }
                html += `
                    <td id="badgeTitle${badge.id}" >${badge.title}</td>
                    <td id="badgeDescription${badge.id}">${badge.description}</td>
                    <td id="badgeConditionType${badge.id}">${badge.conditionEnum}</td>
                    <td id="badgeConditionDate${badge.id}">${badge.conditionFirstDay} ~ ${badge.conditionLastDay}</td>
                    <td id="badgeConditionStandard${badge.id}">${badge.conditionStandard}</td>
                    <td id ="badgeTags${badge.id}">
                        <ul>
                            ${badge.tags.map(tag => `<li>${tag.title}</li>`).join('')}
                        </ul>
                    </td>
                    <td><a onclick="showBadgeInput('${badge.title}', '${badge.id}')">수정</a></td>
                    <td><a onclick="alertBadgeDelete('${badge.title}', '${badge.id}')">삭제</a></td>
                </tr>`;
            });
            $('#badges-table-body').html(html);
            makeBadgePagination(data);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

//페이지네이션
function makeBadgePagination(page){
    let pagination = $("#badges-management-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<button><a onclick='getBadges(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="getBadges(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='getBadges(${cur + 1})'>다음</a></button>`);
    }
}

function addNewRow() {
    const tableBody = document.getElementById('badges-table-body');

    // 새로운 <tr> 요소 생성
    const newRow = document.createElement('tr');

    // 각 열에 대한 <td> 요소 생성
    newRow.innerHTML = `
    <td>.</td>
    <td><input id="badgeImageInput" type="file" accept="image/*" onchange="previewImage(this)">
        <img id="preview" src="" alt="Image Preview" style="max-width: 300px; max-height: 300px;">
    </td>
    <td><input id="badgeTitleInput" type="text" class="input-field" placeholder="Title"></td>
    <td><input id="badgeDescriptionInput" type="text" class="input-field" placeholder="Description"></td>
    <td>
        <select id="badgeTypeInput" class="input-field">
            <option value="FREQUENCY">FREQUENCY</option>
            <option value="STEADY">STEADY</option>
            <option value="TAG">TAG</option>
        </select>
    </td>
    <td>
        <label for="startDate">시작:</label>
        <input type="date" id="badgeStartDate" class="input-field">
        <label for="endDate">종료:</label>
        <input type="date" id="badgeEndDate" class="input-field">
    </td>
    <td><input id="badgeStandardInput" type="number" class="input-field" placeholder="Condition Standard"></td>
    <td>
        <div id="badge-tags-input-container">
        <a id="addBadgeTagInput" onclick="addBadgeTagInput()">추가</a>
        </div>
    </td>
    <td><button class="button blue gradient" onclick="createBadge(this)">생성</button></td>
    <td><button class="button red gradient" onclick="cancelCreateBadge(this)">취소</button></td>
  `;

    // 새로운 행을 테이블 바디에 추가
    tableBody.appendChild(newRow);
}

function previewImage(input) {
    const preview = document.getElementById('preview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            preview.src = e.target.result;
        };

        reader.readAsDataURL(input.files[0]);
    }
}

let tagInputCount = 0; // 추가된 인풋 필드 개수를 추적하기 위한 변수

function addBadgeTagInput() {
    const container = document.getElementById('badge-tags-input-container'); // 인풋 필드를 포함할 컨테이너를 선택

    // 새로운 인풋 필드를 생성합니다.
    const input = document.createElement('input');
    input.type = 'text';
    input.className = 'input-field';
    input.placeholder = 'Condition Tags';

    // 인풋 필드에 고유한 ID를 할당하여 추적합니다.
    input.id = `badgeTagInput${tagInputCount}`;
    tagInputCount++;

    // 컨테이너에 인풋 필드를 추가합니다.
    container.insertBefore(input, document.getElementById('addBadgeTagInput')); // 추가 버튼 위에 삽입
}

function createBadge() {
    // 사용자가 입력한 값들을 수집
    const title = document.querySelector('#badgeTitleInput').value;
    const description = document.querySelector('#badgeDescriptionInput').value;
    const conditionType = document.querySelector('#badgeTypeInput').value;
    const startDate = document.querySelector('#badgeStartDate').value;
    const endDate = document.querySelector('#badgeEndDate').value;
    const conditionStandard = document.querySelector('#badgeStandardInput').value;

    // 태그 값을 수집
    const tagInputs = document.querySelectorAll('#badge-tags-input-container input[type="text"]');
    const tags = [];
    tagInputs.forEach(input => {
        const tagValue = input.value.trim();
        if (tagValue) {
            tags.push({ title: tagValue });
        }
    });

    // 이미지 파일을 수집
    const imageFileInput = document.querySelector('#badgeImageInput');
    const imageFile = imageFileInput.files[0];

    // 이미지 파일을 form-data에 추가
    const formData = new FormData();
    if (imageFile) {
        formData.append('files', imageFile);
    }

    // requestDto 객체에 나머지 내용 추가
    const requestDto = {
        title: title,
        description: description,
        conditionEnum: conditionType,
        conditionFirstDay: startDate,
        conditionLastDay: endDate,
        conditionStandard: conditionStandard,
    };

    // 태그 값이 있는 경우에만 conditionTags 속성을 추가
    if (tags.length > 0) {
        requestDto.conditionTags = tags;
    }

    // JSON 문자열을 Blob 객체로 변환하여 FormData에 추가
    const jsonRequestDto = JSON.stringify(requestDto);
    const jsonBlob = new Blob([jsonRequestDto], {type: 'application/json'});
    formData.append('requestDto', jsonBlob);

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/admin/badges`,
        type: 'POST',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            alert(data.statusMessage);
            getBadges();
        },
        error: function (err) {
            alert(err.responseText.statusMessage);
            console.log('Error:', err);
        }
    });
}

function cancelCreateBadge(button) {
    // Delete 버튼을 누르면 해당 행을 삭제할 수 있습니다.
    const row = button.parentElement.parentElement;
    row.remove();
}

let tagContainer;

function showBadgeInput(title, id) {
    // 해당 테이블 셀을 찾습니다.
    const imageCell = $(`#badgeImage${id}`);
    const titleCell = $(`#badgeTitle${id}`);
    const descriptionCell = $(`#badgeDescription${id}`);
    const typeCell = $(`#badgeConditionType${id}`);
    const dateCell = $(`#badgeConditionDate${id}`);
    const standardCell = $(`#badgeConditionStandard${id}`);
    const tagsCell = $(`#badgeTags${id}`);

    // 셀 내용을 현재 내용으로 저장합니다.
    const currentImageSrc = imageCell.find('img').attr('src');
    const currentTitle = titleCell.text();
    const currentDescription = descriptionCell.text();
    const currentType = typeCell.text();
    const dateRangeText = dateCell.text();
    // "~" 문자를 기준으로 문자열을 나눕니다.
    const dateRangeArray = dateRangeText.split(' ~ ');
    const currentStartDate = dateRangeArray[0].trim(); // 시작일
    const currentEndDate = dateRangeArray[1].trim();   // 종료일
    const currentStandard = standardCell.text();
    const currentTagsList = tagsCell.find('li'); // li 요소들을 가져와 리스트로 만듭니다.

    // input 요소를 생성하고 현재 내용으로 초기화합니다.
    const imageInput = $('<input>', {
        type: 'file',
        id: `badgeImageInput${id}`,
    });

    const imageElement = $('<img>', {
        src: currentImageSrc,
        alt: 'Badge Image',
        width: 100,
        id: `badgeImage${id}`,
    });

    const titleInput = $('<input>', {
        type: 'text',
        id: `badgeTitleInput${id}`,
        value: currentTitle
    });

    const descriptionInput = $('<input>', {
        type: 'text',
        id: `badgeDescriptionInput${id}`,
        value: currentDescription
    });

    const typeInput = $('<select>', {
        id: `badgeTypeInput${id}`,
        class: 'input-field'
    });

    // 각각의 옵션을 생성하고 현재 내용과 일치하는 옵션을 선택합니다.
    const optionFrequency = $('<option>', {
        value: 'FREQUENCY',
        text: 'FREQUENCY'
    });

    const optionSteady = $('<option>', {
        value: 'STEADY',
        text: 'STEADY'
    });

    const optionTag = $('<option>', {
        value: 'TAG',
        text: 'TAG'
    });

    // 현재 선택된 타입에 따라 해당 옵션을 선택 상태로 설정합니다.
    if (currentType === 'FREQUENCY') {
        optionFrequency.attr('selected', 'selected');
    } else if (currentType === 'STEADY') {
        optionSteady.attr('selected', 'selected');
    } else if (currentType === 'TAG') {
        optionTag.attr('selected', 'selected');
    }

    // 옵션을 <select> 요소에 추가합니다.
    typeInput.append(optionFrequency);
    typeInput.append(optionSteady);
    typeInput.append(optionTag);

    const startDateInput = $('<input>', {
        type: 'date',
        id: `badgeStartDateInput${id}`,
        value: currentStartDate
    });

    const endDateInput = $('<input>', {
        type: 'date',
        id: `badgeEndDateInput${id}`,
        value: currentEndDate
    });

    const standardInput = $('<input>', {
        type: 'number',
        id: `badgeStandardInput${id}`,
        value: currentStandard
    });

    // 각 태그에 대한 입력 요소를 생성하고, 리스트를 배열로 변환합니다.
    const tagInputs = currentTagsList.map((index, tagElement) => {
        const tagText = $(tagElement).text(); // 각 li 요소의 텍스트를 가져옵니다.
        const tagInput = $('<input>', {
            type: 'text',
            value: tagText // 각 태그의 값을 입력 요소의 기본 값으로 설정
        });

        // 삭제 버튼을 생성합니다.
        const removeTagButton = $('<button>', {
            text: '삭제',
            click: function () {
                // 삭제 버튼을 클릭했을 때 해당 입력 필드와 삭제 버튼을 함께 제거합니다.
                tagInput.remove();
                removeTagButton.remove();
            }
        });

        // 입력 요소와 삭제 버튼을 함께 감싸는 div를 생성합니다.
        const inputContainer = $('<div>').append(tagInput).append(removeTagButton);

        return inputContainer; // 입력 요소와 삭제 버튼을 포함한 div를 반환합니다.
    }).get(); // 배열로 변환합니다.

    // 기존 태그 입력 요소를 컨테이너에 넣습니다.
    tagContainer = $('<div>').append(tagInputs);

    // "태그 추가" 버튼을 생성합니다.
    const addTagButton = $('<a>', {
        type: 'button',
        text: '태그 추가',
        id: `addTagButton${id}`
    });

    // "태그 추가" 버튼 클릭 이벤트 핸들러를 추가합니다.
    addTagButton.on('click', function () {
        // 새로운 태그 입력 필드를 생성합니다.
        const newTagInput = $('<input>', {
            type: 'text',
            class: 'tag-input' // 필요한 스타일을 적용할 클래스를 지정합니다.
        });

        // 새로운 태그 입력 필드와 삭제 버튼을 생성합니다.
        const newInputContainer = $('<div>').append(newTagInput);
        const removeTagButton = $('<button>', {
            text: '삭제',
            click: function () {
                // 삭제 버튼을 클릭했을 때 해당 입력 필드와 삭제 버튼을 함께 제거합니다.
                newTagInput.remove();
                removeTagButton.remove();
            }
        });

        // 새로운 태그 입력 필드와 삭제 버튼을 컨테이너에 추가합니다.
        tagContainer.append(newInputContainer);
        tagContainer.append(removeTagButton);
    });

    // 현재 셀을 비워주고 input 요소를 추가합니다.
    imageCell.empty().append(imageInput).append(imageElement);
    titleCell.empty().append(titleInput);
    descriptionCell.empty().append(descriptionInput);
    typeCell.empty().append(typeInput);
    dateCell.empty().append(startDateInput).append(' ~ ').append(endDateInput);
    standardCell.empty().append(standardInput);
    tagsCell.empty().append(tagContainer).append(addTagButton);

    // 수정 버튼을 '확인'으로 변경합니다.
    tagsCell.next().html('<a onclick="alertBadgeModify(\'' + currentTitle + '\', ' + id + ')">확인</a>');

    // 삭제 버튼을 '취소'로 변경합니다.
    tagsCell.next().next().html('<a onclick="getBadges()">취소</a>');

    // 파일 선택(input) 요소의 변경 이벤트를 감지
    $('#badgeImageInput' + id).on('change', function (e) {
        const file = e.target.files[0]; // 선택한 파일 가져오기

        if (file) {
            const reader = new FileReader();

            reader.onload = function (e) {
                // 읽어온 파일을 이미지 요소에 표시
                imageElement.attr('src', e.target.result);
            };

            // 파일을 읽어오기
            reader.readAsDataURL(file);
        }
    });

}

function alertBadgeModify(badgeName, badgeId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + badgeName + "' 뱃지를 수정하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        modifyBadge(badgeId);
    } else {
        getBadges();
    }
}

function modifyBadge(badgeId) {
    const title = document.getElementById(`badgeTitleInput${badgeId}`).value;
    const description = document.getElementById(`badgeDescriptionInput${badgeId}`).value;
    const conditionType = document.getElementById(`badgeTypeInput${badgeId}`).value;
    const startDate = document.getElementById(`badgeStartDateInput${badgeId}`).value;
    const endDate = document.getElementById(`badgeEndDateInput${badgeId}`).value;
    const conditionStandard = document.getElementById(`badgeStandardInput${badgeId}`).value;

    // tagContainer 내부의 모든 input 요소 선택
    const tagInputsInContainer = tagContainer.find('input[type="text"]');

    // 각 input 요소의 값을 가져와서 배열로 저장
    const tagValues = tagInputsInContainer.map(function () {
        return $(this).val();
    }).get();

    // 태그 값을 수집
    const tags = [];
    tagValues.forEach(tagValue => {
        if (tagValue.trim()) {
            tags.push({ title: tagValue.trim() });
        }
    });

    // 이미지 파일을 수집
    const imageFileInput = document.getElementById(`badgeImageInput${badgeId}`);
    const imageFile = imageFileInput.files[0];

    // 이미지 파일을 form-data에 추가
    const formData = new FormData();
    if (imageFile) {
        formData.append('files', imageFile);
    }

    const requestDto = {
        title: title,
        description: description,
        conditionEnum: conditionType,
        conditionFirstDay: startDate,
        conditionLastDay: endDate,
        conditionStandard: conditionStandard,
    };

    // 태그 값이 있는 경우에만 conditionTags 속성을 추가
    if (tags.length > 0) {
        requestDto.conditionTags = tags;
    }

    // JSON 문자열을 Blob 객체로 변환하여 FormData에 추가
    const jsonRequestDto = JSON.stringify(requestDto);
    const jsonBlob = new Blob([jsonRequestDto], {type: 'application/json'});
    formData.append('requestDto', jsonBlob);

    $.ajax({
        url: `/api/admin/badges/${badgeId}`,
        type: 'PATCH',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function () {
            alert('뱃지 수정이 완료되었습니다.');
            getBadges();
        },
        error: function (err) {
            alert(err.responseText.statusMessage);
            console.log('Error:', err);
        }
    });
}


function alertBadgeDelete(badgeTitle, badgeId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + badgeTitle + "' 뱃지를 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        deleteBadge(badgeId);
    }
}

function deleteBadge(badgeId) {
    $.ajax({
        url: `/api/admin/badges/${badgeId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            getBadges();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}