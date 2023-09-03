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
