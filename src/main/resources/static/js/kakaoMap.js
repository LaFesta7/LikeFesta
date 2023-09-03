document.addEventListener('DOMContentLoaded', function() {
    // Your previous code
    const FESTIVAL_TABLE_BODY = document.getElementById('festival-table-body');
    const PAGINATION = document.getElementById('pagination');
    const TOTAL_PAGES = 6;
    let paginationLinks = '';

    for (let page = 1; page <= TOTAL_PAGES; page++) {
        paginationLinks += `<a href="?page=${page}">${page}</a>`;
    }

    PAGINATION.innerHTML = paginationLinks;

    const token = "7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg=="; // Replace this with the actual token value
    const myButton = document.getElementById("myButton");

    if(myButton) {  // Check if myButton is not null
        myButton.addEventListener("click", function() {
            fetch("/api/users/profile/followings", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                },
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error("Error:", error));
        });
    }

    const button = document.getElementById('getTagsBtn');

    if(button) {
        button.addEventListener('click', function() {
            if (this.classList.contains('myButton')) {
            }
        });
    }
});
