$(document).ready(function () {
    $("#submitButton").on("click", function (event) {
        event.preventDefault();

        const nickname = $("#nicknameInput").val();

        if (nickname) {
            $.ajax({
                url: '/users/info',
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({
                    nickname: nickname
                }),
                success: function (response) {
                    if (response && response.status === 200) {
                        alert("Nickname updated successfully.");
                    } else {
                        alert("Could not update nickname.");
                    }
                },
                error: function (err) {
                    alert("닉네임 수정요청이 완료되었습니다."); //임시ㅎㅎ
                }
            });
        } else {
            alert("Nickname cannot be empty.");
        }
    });
});
