const $backBtn = document.getElementsByClassName("back-btn")[0];
const $updateBtn = document.getElementsByClassName("update-btn")[0];
const $modal = document.getElementById("update-modal");
const $close = document.getElementById("close");
const $updateSubmit = document.getElementById("update-submit");
const $updateAlbumNameInput = document.getElementById("albumName");
const $addBtn = document.getElementById("add-btn");
const $checkBtn = document.getElementsByClassName("check-btn")[0];
const $move = document.getElementsByClassName("move")[0];
const $download = document.getElementsByClassName("download")[0];
const $bin = document.getElementsByClassName("bin")[0];

$backBtn.addEventListener("click", () => {
  location.href = "/albums";
});

$updateBtn.addEventListener("click", () => {
  $modal.classList.add("show");
  $modal.style.display = "block";
});

$close.addEventListener("click", () => {
  $modal.classList.remove("show");
  $modal.style.display = "none";
});

$addBtn.addEventListener("click", () => {
  location.href = "/albums";
});

$checkBtn.addEventListener("click", () => {

});