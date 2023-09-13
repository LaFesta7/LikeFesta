$(document).ready(function () {
    getBadges();
});

function getBadges() {
    $.ajax({
        url: '/api/users/badges',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            // API 요청 성공 시 결과 데이터를 처리합니다.
            const badgeList = $('#badgeList');
            // 데이터를 반복하여 뱃지 목록에 추가합니다.
            data.forEach(function (badge) {
                const listItem = $('<li>');
                // 텍스트를 하얀색으로 변경
                listItem.css('color', 'white');
                listItem.css('font-size', '20px');
                listItem.css('margin-bottom', '10px');
                // 대표 박스 생성
                const representative = $('<input>');
                representative.attr('type', 'submit');
                representative.attr('data-badge-id', badge.id); // 뱃지 ID를 데이터 속성에 저장
                // badge.representative 값에 따라 체크 여부 설정
                if (badge.representative) {
                    representative.val('대표'); // 대표인 경우 "대표" 텍스트 설정
                    representative.css('background-color', 'green');
                    representative.css('color', 'white');
                } else {
                    representative.val('설정');
                    representative.css('background-color', 'red');
                    representative.css('color', 'white');
                }

                // 클릭 이벤트 추가
                representative.click(function () {
                    const badgeId = $(this).data('badge-id');
                    editRepresentativeBadge(badgeId);
                });

                listItem.text(badge.title);
                // 대표 박스를 listItem 앞에 추가
                listItem.prepend(representative);
                badgeList.append(listItem);
            });
        },
        error: function (error) {
            console.error('API 요청 중 오류 발생:', error);
        }
    });
}

function editRepresentativeBadge(badgeId) {
    $.ajax({
        url: `/api/users/badges/${badgeId}`,
        method: 'PATCH',
        success: function (data) {
            getBadges();
        },
        error: function (err) {
            alert(err.responseJSON.statusMessage);
            console.log('Error:', err);
        }
    });
}