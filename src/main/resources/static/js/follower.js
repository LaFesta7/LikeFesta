let userId = localStorage.getItem('userId');

function loadFollowers() {
    $.ajax({
        url: `/api/users/${userId}/follows/followings`,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            let followerListHTML = '';
            data.forEach(follower => {
                followerListHTML += `
          <div class="follower-item">
            <span>${follower.followedUser.name}</span> 
            <button class="follow-button" onclick="toggleFollow(${follower.followedUser.id})">❤</button>
          </div>`;
            });
            $('#followerList').html(followerListHTML);
        },
        error: function(error) {
            console.log("An error occurred:", error);
        }
    });
}

function toggleFollow(followingUserId) {
    $.ajax({
        url: `/api/users/${userId}/follows/${followingUserId}`,
        type: 'POST',
        success: function() {
            loadFollowers();
        },
        error: function(err) {

            if (err.status === 400) {
                $.ajax({
                    url: `/api/users/${userId}/follows/${followingUserId}`,
                    type: 'DELETE',
                    success: function() {
                        loadFollowers();
                    },
                    error: function(error) {
                        console.log("An error occurred:", error);
                    }
                });
            }
        }
    });
}

// When the page loads, load the follow list.
$(document).ready(function() {
    loadFollowers();
});

document.addEventListener("DOMContentLoaded", function() {
    // 예시 팔로워 목록
    const followers = ['Alice', 'Bob', 'Charlie'];

    // 팔로워 목록과 팔로우 취소 버튼을 채웁니다.
    const followerList = document.getElementById('followers');
    followers.forEach(follower => {
        const listItem = document.createElement('li');
        listItem.textContent = follower;

        const unfollowButton = document.createElement('button');
        unfollowButton.textContent = '팔로우 취소';
        unfollowButton.addEventListener('click', function() {
            unfollow(follower);
        });

        listItem.appendChild(unfollowButton);
        followerList.appendChild(listItem);
    });
});

// 팔로우 취소 동작을 처리하는 함수
function unfollow(followerName) {
    // 팔로워 목록에서 팔로워를 제거합니다 (필요하면 구현)
    // 상태 창을 업데이트합니다.
    const status = document.getElementById('status');
    status.textContent = `${followerName} 님을 팔로우 취소하였습니다.`;
}