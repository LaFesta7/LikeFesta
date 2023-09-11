$(document).ready(function () {
    $(document).ready(function () {
        $.ajax({
            url: `/api/users/info`,
            type: 'GET',
            success: function (data) {
                console.log(data);
                let html = `
                    <div class="user-box">
                        <input type="text" id="nicknameInput" required="" value="${data.nickname}">
                        <label for="nicknameInput">Nickname</label>
                    </div>
                    <div class="right" style="margin-top: -60px"><a href="#" onclick="nicknameEdit()">수정</a></div>
                     <div class="user-box">
                        <input type="text" id="introduceInput" required="" value="${data.introduce}">
                        <label for="introduceInput">Introduce</label>
                    </div>
                    <div class="right" style="margin-top: -60px"><a href="#" onclick="introduceEdit()">수정</a></div>
                    <div class="user-box">
                        <input type="email" id="emailInput" required="" value="${data.email}">
                        <label for="emailInput">Email</label>
                    </div>
                        <button type="button" id="editEmailSendVerificationBtn" onclick="editEmailSendVerificationEmail()">인증메일 발송</button>
                        <!-- 이메일 인증키 입력칸 (기본적으로 숨김) -->
                        <div id="editEmailConfirmBox" style="display: none;">
                            <input type="text" id="editEmailVerificationCodeInput" required>
                            <label>인증코드</label><br>
                            <button type="button" onclick="editEmailConfirmVerificationCode()">인증확인</button>
                            <p id="timer1" style="color:white;">3:00</p>
                        </div>
                    <div class="right" style="margin-top: -60px"><a href="#" onclick="emailEdit()">수정</a></div>
                    <div class="user-box">
                    </div>
                    <div class="user-box">
                        <p style="top: -20px; left: 0; color: #03e9f4; font-size: 12px;">Password</p>
                        <input type="password" id="currentPasswordInput" required="" placeholder="현재 비밀번호를 입력해주세요.">
                        <input type="password" id="newPasswordInput" required="" placeholder="변경할 비밀번호를 입력해주세요">
                    </div>
                        <button type="button" id="sendVerificationBtn" onclick="editPasswordSendVerificationEmail()">(현재 비밀번호 분실 시) 인증메일 발송</button>
                        <!-- 이메일 인증키 입력칸 (기본적으로 숨김) -->
                        <div id="editPasswordConfirmBox" style="display: none;">
                            <input type="text" id="editPasswordVerificationCodeInput" required>
                            <label>인증코드</label><br>
                            <button type="button" onclick="editPasswordConfirmVerificationCode('${data.email}')">인증확인</button>
                            <p id="timer2" style="color:white;">3:00</p>
                        </div>
                    <div class="right" style="margin-top: -60px"><a href="#" onclick="passwordEdit()">수정</a></div>
                    <div class="user-box">
                        <p style="top: -20px; left: 0; color: #03e9f4; font-size: 12px;">Image</p>
                        <p id="imageName" style="font-size: 16px; color: white">${data.files[0].keyName}</p>
                        <div class="circular-image">
                            <img id="preview" 
                                src="${data.files[0] ? data.files[0].uploadFileUrl 
                                : 'https://vignette.wikia.nocookie.net/the-sun-vanished/images/5/5d/Twitter-avi-gender-balanced-figure.png/revision/latest?cb=20180713020754'}" 
                                alt="Image Preview" style="width: 100%; height: 100%; object-fit: cover;">
                        </div>
                        <input id="imageInput" type="file" accept="image/*" onchange="previewImage(this)">
                    </div>
                    <div class="right" style="margin-top: -60px"><a href="#" onclick="imageEdit()">수정</a></div>
                `;
                $('#profile-edit').html(html);
            },
            error: function (err) {
                console.log('Error:', err);
            }
        });
    });

    $("#editEmailSendVerificationBtn").click(function () {
        $("#editEmailConfirmBox").show();
    });

    $("#editPasswordSendVerificationBtn").click(function () {
        $("#editPasswordConfirmBox").show();
    });
});

// 이메일과 비밀번호 유효성 검사를 위한 정규 표현식
const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/; // 최소 하나의 소문자, 하나의 대문자, 하나의 숫자가 포함되어 있고 최소 8자 이상

// 타이머 변수
let timer1;
let timer2;
let seconds = 180; // 3분

// 카운트다운 타이머를 시작하는 함수
function startCountdown(timer, timerElement) {
    timer = setInterval(() => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        $(timerElement).text(`${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`);
        if (seconds === 0) {
            clearInterval(timer);
            // // 인증과 관련된 UI 요소를 리셋
            // $('#editEmailConfirmBox').hide();
            // $('#editEmailSendVerificationBtn').show();
            // $('#editPasswordConfirmBox').hide();
            // $('#editPasswordSendVerificationBtn').show();
            alert('인증 시간이 만료되었습니다. 인증 이메일을 다시 보내주세요.');
        }
        seconds--;
    }, 1000);
}

// 타이머를 초기화하는 함수
function resetTimer(timer) {
    clearInterval(timer);
    seconds = 180; // 3분
    $('#timer1').text("3:00");
    $('#timer2').text("3:00");
}

function previewImage(input) {
    const preview = document.getElementById('preview');
    const imageName = document.getElementById('imageName');

    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            // 미리보기 이미지 업데이트
            preview.src = e.target.result;
            // 이미지 이름 업데이트
            imageName.textContent = input.files[0].name;
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function nicknameEdit() {
    const nickname = $("#nicknameInput").val();

    if (nickname) {
        $.ajax({
            url: '/api/users/info/nickname',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                nickname: nickname
            }),
            success: function (response) {
                alert(response.statusMessage);
            },
            error: function (err) {
                alert(err.statusMessage);
            }
        });
    } else {
        alert("Nickname cannot be empty.");
    }
}

function introduceEdit() {
    const introduce = $("#introduceInput").val();

    if (introduce) {
        $.ajax({
            url: '/api/users/info/introduce',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                introduce: introduce
            }),
            success: function (response) {
                alert(response.statusMessage);
            },
            error: function (err) {
                alert(err.statusMessage);
            }
        });
    } else {
        alert("Introduce cannot be empty.");
    }
}

function emailEdit() {
    const email = $("#emailInput").val();

    if (email) {
        $.ajax({
            url: '/api/users/info/email',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email
            }),
            success: function (response) {
                alert(response.statusMessage);
            },
            error: function (err) {
                alert(err.statusMessage);
            }
        });
    } else {
        alert("Email cannot be empty.");
    }
}

async function editEmailSendVerificationEmail() {
    const email = $("#emailInput").val();
    if (!emailRegex.test(email)) {
        alert('유효한 이메일을 입력해주세요.');
        return;
    }

    const response = await fetch('/api/users/mail-confirm', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email
        })
    });
    if (response.ok) {
        $('#editEmailConfirmBox').show();
        $('#editEmailSendVerificationBtn').hide();
        startCountdown(timer1, '#timer1'); // 타이머1 시작
        const responseData = await response.json();
        console.log(responseData.statusMessage);
    } else {
        $('#editEmailConfirmBox').hide();
        $('#editEmailSendVerificationBtn').show();
        const responseData = await response.json();
        console.error(responseData.statusMessage);
        alert(responseData.statusMessage);
    }
}

async function editEmailConfirmVerificationCode() {
    const email = $("#emailInput").val();
    const verificationCode = $("#editEmailVerificationCodeInput").val();
    await confirmVerificationCode(email, verificationCode);
}

async function editPasswordSendVerificationEmail() {
    const response = await fetch('/api/users/mail-confirm-password', {
        method: 'POST'
    });
    if (response.ok) {
        $('#editPasswordConfirmBox').show();
        $('#editPasswordSendVerificationBtn').hide();
        startCountdown(timer2, '#timer2'); // 타이머2 시작
        const responseData = await response.json();
        console.log(responseData.statusMessage);
    } else {
        $('#editPasswordConfirmBox').hide();
        $('#editPasswordSendVerificationBtn').show();
        const responseData = await response.json();
        console.error(responseData.statusMessage);
        alert(responseData.statusMessage);
    }
}

async function editPasswordConfirmVerificationCode(email) {
    const verificationCode = $("#editPasswordVerificationCodeInput").val();
    await confirmVerificationCode(email, verificationCode);
}

function passwordEdit() {
    const currentPassword = $("#currentPasswordInput").val();
    const newPassword = $("#newPasswordInput").val();

    if (newPassword) {
        if (!passwordRegex.test(newPassword)) {
            alert('비밀번호는 적어도 하나의 영어 문자, 하나의 숫자, 하나의 특수 문자를 포함해야 하며, 적어도 8자 이상이어야 합니다.');
            return;
        }

        $.ajax({
            url: '/api/users/info/password',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                currentPassword: currentPassword,
                newPassword: newPassword
            }),
            success: function (response) {
                alert(response.statusMessage);
            },
            error: function (err) {
                alert(err.statusMessage);
            }
        });
    } else {
        alert("New Password cannot be empty.");
    }
}

function imageEdit() {
    // 이미지 파일을 수집
    const imageFileInput = document.querySelector('#imageInput');
    const imageFile = imageFileInput.files[0];

    // 이미지 파일을 form-data에 추가
    const formData = new FormData();
    if (imageFile) {
        formData.append('files', imageFile);
    }

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/users/info/image`,
        type: 'PUT',
        data: formData, // form-data 전송
        contentType: false, // form-data는 contentType을 false로 설정
        processData: false, // 이미지 파일을 전송할 때 false로 설정
        success: function (data) {
            alert(data.statusMessage);
        },
        error: function (err) {
            alert(err.responseText.statusMessage);
            console.log('Error:', err);
        }
    });
}

async function confirmVerificationCode(email, verificationCode) {
    if (verificationCode) {
        // 인증 확인
        const verifyResponse = await fetch('/api/users/verify-code', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                verificationCode: verificationCode
            })
        });

        if (verifyResponse.ok) {
            resetTimer(timer1); // 타이머 초기화
            resetTimer(timer2); // 타이머 초기화
            const responseData = await verifyResponse.json();
            console.log(responseData.statusMessage);
            alert(responseData.statusMessage);
            $('#editPasswordConfirmBox').hide();
        } else {
            const responseData = await verifyResponse.json();
            console.error(responseData.statusMessage);
            alert(responseData.statusMessage);
        }
    } else {
        alert('인증 코드를 입력해주세요.');
    }
}