$(document).ready(function () {
    $.ajax({
        url: `/api/users/${userId}/badges`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            data.forEach(function (badge) {
                html += `<div class="badge-item">${badge.name}</div>`;
            });
            $('#badgeList').append(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});

document.addEventListener('DOMContentLoaded', () => {
    fetchBadges();
    document.getElementById('setPrimaryBadge').addEventListener('click', () => {
        if (selectedBadgeId) {
            // 대표 벳찌를 설정하는 API를 호출
            // 예를 들어: fetch(`/api/setPrimaryBadge/${selectedBadgeId}`, { method: 'POST' });
            alert(`대표 벳찌가 설정되었습니다: ${selectedBadgeId}`);
        } else {
            alert('대표 벳찌를 선택해주세요.');
        }
    });
});