//// JavaScript로 로그인 버튼 클릭 이벤트 처리
//document.getElementById("login-btn").addEventListener("click", function(event) {
//    // 기본 동작(페이지 이동)을 막음
//    event.preventDefault();
//    // 로그인 성공 시 coupon.html 페이지로 이동
//    window.location.href = "coupon.html";
//});

document.getElementById("login-btn").addEventListener("click", function() {
    event.preventDefault();
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var userData = {
        name: username,
        pw: password
    };

    fetch('/coupon/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (response.ok) {
            console.log("로그인 성공");
            window.location.href = "/coupon/coupon";
        } else {
            console.error("로그인 실패");
            alert("로그인 실패. 아이디와 패스워드를 확인해주세요.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    });
});
