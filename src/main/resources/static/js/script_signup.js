// script_signup.js

document.addEventListener("DOMContentLoaded", function () {
    var loginBtn = document.getElementById("login-btn");

    loginBtn.addEventListener("click", function () {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;

        var userData = {
            name: username,
            pw: password
        };

        // AJAX 요청
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/coupon/signup", true);
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 201) {
                    // 성공적으로 가입된 경우
                    console.log("가입 성공");
                    // 로그인 성공 시 login.html 페이지로 이동
                    window.location.href = "/coupon/login";
                } else {
                    // 가입 실패
                    console.error("가입 실패");
                    alert("이미 존재하는 유저입니다.");
                }
            }
        };

        // 객체를 JSON 문자열로 변환하여 전송
        xhr.send(JSON.stringify(userData));
    });
});