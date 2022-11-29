function handleTabs() {
  for (
    let i = 0;
    i < document.querySelectorAll(".cmp-tabs__tab .nav-link").length;
    i++
  ) {
    document
      .querySelectorAll(".cmp-tabs__tab .nav-link")
      [i].removeAttribute("tabindex");
  }
}
window.addEventListener("load", function () {
  ["keypress", "focus", "click"].forEach((evt) => {
    for (
      let i = 0;
      i < document.querySelectorAll(".cmp-tabs__tab .nav-link").length;
      i++
    ) {
      document
        .querySelectorAll(".cmp-tabs__tab .nav-link")
        [i].addEventListener(evt, handleTabs);
    }
  });
});
