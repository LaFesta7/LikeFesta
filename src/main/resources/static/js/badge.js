let selectedBadgeId = null;  // 선택된 벳찌 아이디

// 서버에서 벳찌 정보를 불러오는 함수 (가상 코드)
async function fetchBadges() {
    // 여기서 실제 API 호출을 하시면 됩니다.
    // 예를 들어: const response = await fetch("/api/badges");
    // 예시 데이터
    const badges = [
        {id: 1, name: '축제의 달인'},
        {id: 2, name: '라페스타 개근'},
        {id: 3, name: '리뷰의 달인'}
    ];

    const badgeList = document.getElementById('badgeList');
    badges.forEach(badge => {
        const badgeItem = document.createElement('div');
        badgeItem.innerHTML = `
                <input type="radio" name="badge" value="${badge.id}" />
                ${badge.name}
            `;
        badgeList.appendChild(badgeItem);

        badgeItem.querySelector('input').addEventListener('click', () => {
            selectedBadgeId = badge.id;
        });
    });
}

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