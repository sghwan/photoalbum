//새 앨범 생성 클릭시 모달 오픈
const $create = document.getElementById("create");
const $modal = document.getElementById("create-modal");
const $close = document.getElementById("close");
const $createSubmit = document.getElementById("create-submit");
const $createAlbumInput = document.getElementById("create-album-input");

$create.addEventListener("click", () => {
  $modal.classList.add("show");
  $modal.style.display = "block";
});

$close.addEventListener("click", () => {
  $modal.classList.remove("show");
  $modal.style.display = "none";
});

$createSubmit.addEventListener("click", () => {
  $createAlbumInput.value = "";
  $modal.classList.remove("show");
  $modal.style.display = "none";
});
