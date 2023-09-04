$(document).ready(function () {
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
                <div  class="post-header">
    <div class="post-title">${data.title}</div>
    <div class="post-meta">${data.username}}</div>
    <div class="post-meta">${data.createdtAt}</div>
  </div >
  <img src="/images/best2.jpg" alt="Review Image" class="post-image">
  <div class="post-content">
        ${data.content}
  </div>
  <div class="actions">
    <div class="like-button">${data.like.size}</div>
    <div class="edit-delete">
      <a href="review-edit.html" class="edit">Edit</a>
      <button class="delete" onclick="deletePost()">Delete</button>
    </div>
  </div>
  <div id="review-comment">
  </div>
                `;
            $('#review-page').html(html);
            $.ajax({
                url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments`,
                type: 'GET',
                success: function (data) {
                    console.log(data);
                    let html = '';
                    for (let i = 0; i <data.length; i++) {
                        html += `
                        <div class="comment">
                          <div class="comment-author">${data[i].username}</div>
                          <div class="comment-content">${data[i].content}</div>
                        </div>    
                        `;
                    }
                    $('#review-comment').html(html);
                },
                error: function (err) {
                    console.log('Error:', err);
                }
            });
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});