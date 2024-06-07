let toggle = document.querySelector("#header .toggle-button");
let nav = document.querySelector("#header .navbar");
toggle.addEventListener('click',function(){
    if(nav.style.display === "none")
    { nav.style.display = "initial";}
    else
    {
        nav.style.display = "none";
    }
});


//with masonry library for horizontal grid
$('.grid').masonry({
  itemSelector:'.grid-item',
  columnWidth:20    
});


//initialize swiper slider
const swiper = new Swiper('.swiper', {
  // Optional parameters
  direction: 'horizontal',
  loop: true,
  slidesPerView:1,
  spaceBetween:10,
  
  autoplay:{
      delay:1000
  },
  //responsive brakpoints
  breakpoints:{
    // when window width is >= 320px
    320: {
      slidesPerView: 2,
      spaceBetween: 5
    },
    // when window width is >= 888px
    888: {
      slidesPerView: 2,
      spaceBetween: 10
    },
    // when window width is >= 1110px
    1110: {
      slidesPerView: 3,
      spaceBetween: 10
    },
    1330:{
      slidesPerView: 5,
      spaceBetween: 10
    }
  }
});


//animation on scroll
//create instance of animation
AOS.init();

//booked event

