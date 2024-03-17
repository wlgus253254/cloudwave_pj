//script_coupon.js

document.getElementById("issue-btn").addEventListener("click", function() {
    fetch('/coupon/issue-coupon', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
    .then(response => {
        if (response.ok) {
            return response.text()
                .then(data => {
                    alert(data); // 서버에서 받은 메시지를 alert으로 표시
                });
        } else {
            throw new Error('서버 응답 실패');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
});
