function navigate(element, path) {
    // 모든 아이템에서 'active' 클래스 제거
    const items = document.querySelectorAll(".nav-item");
    items.forEach(item => item.classList.remove("active"));

    // 클릭된 아이템에만 'active' 클래스 추가
    element.classList.add("active");

    // 👉 실제로 페이지를 이동하려면 아래 주석을 해제하세요.
    // window.location.href = path;
    console.log("이동 경로:", path);
}