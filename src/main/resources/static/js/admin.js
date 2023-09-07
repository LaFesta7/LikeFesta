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
        getOrganizerRequests();
        getFestivalRequests();
        getUsers();
    }
});

// 주최자 승인 요청
function getOrganizerRequests() {
    $.ajax({
        url: '/api/admin/users/organizer-requests',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) { // Loop through each festival
                html += `<tr>
                    <td>${data[i].id}</td>
                    <td>${data[i].username}</td>
                    <td>${data[i].nickname}</td>
                    <td>${data[i].email}</td>
                    <td><a id="checkbox${i}" onclick="alertOrganizerRequest('${data[i].username}', '${data[i].id}')">승인</a></td>
                </tr>`;
            }
            ;
            $('#organizer-request-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
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
function getFestivalRequests() {
    $.ajax({
        url: '/api/admin/festival-requests',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) { // Loop through each festival
                html += `<tr>
                        <td>${data[i].id}</td>
                        <td><a href="/api/festival-requests/${data[i].id}" target="_blank">${data[i].title}</a></td>
                        <td>${data[i].place}</td>
                        <td>${data[i].content}</td>
                        <td>${data[i].openDate} ~ ${data[i].endDate}</td>
                        <td><a href="${data[i].officialLink}" target="_blank">Official Link</a></td>
                        <td>${data[i].nickname}</td>
                        <td><input type="checkbox" onclick="toggleCheckbox(${data[i].id})"></td>
                    </tr>`;
            };
            $('#festival-request-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
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
function getUsers() {
    $.ajax({
        url: '/api/admin/users',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) { // Loop through each festival
                html += `<tr>
                    <td>${data[i].id}</td>
                    <td>${data[i].role}</td>
                    <td>${data[i].username}</td>
                    <td>${data[i].nickname}</td>
                    <td>${data[i].email}</td>
                    <td><a id="checkbox${i}" onclick="alertUserWithdrawal('${data[i].username}', '${data[i].id}')">탈퇴</a></td>
                </tr>`;
            }
            ;
            $('#users-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function alertUserWithdrawal(username, userId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + username + "'를 탈퇴 처리하시겠습니까?");

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