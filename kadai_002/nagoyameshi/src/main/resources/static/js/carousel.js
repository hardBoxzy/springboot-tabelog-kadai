  // カルーセル
  $('.carousel-fade-1').slick({
    autoplay: true,
    infinite: true,
    autoplaySpeed: 2500,
    arrows: false,
        fade: true, 
    slidesToShow: 1,
    slidesToScroll: 1,

    speed: 2000,
    
      responsive: [
    {
      breakpoint: 768, // 画面幅が768px以下になったら
      settings: {
        slidesToShow: 1,   // 1分割（1枚表示）に変える
        slidesToScroll: 1  // 1枚ずつスクロールする
      }
    }
  ]
  });
  
    // カルーセル
  $('.carousel-fade-2').slick({
    autoplay: true,
    infinite: true,
    autoplaySpeed: 2300,
    arrows: false,
        fade: true, 
    slidesToShow: 1,
    slidesToScroll: 1,

    speed: 2000,
    
      responsive: [
    {
      breakpoint: 768, // 画面幅が768px以下になったら
      settings: {
        slidesToShow: 1,   // 1分割（1枚表示）に変える
        slidesToScroll: 1  // 1枚ずつスクロールする
      }
    }
  ]
  });
  
    // カルーセル
  $('.carousel').slick({
    autoplay: true,
    dots: true,
    infinite: true,
    autoplaySpeed: 3000,
    arrows: false,
    
    slidesToShow: 3,
    slidesToScroll: 3,
    fade: false, 
    speed: 2000,
    
      responsive: [
    {
      breakpoint: 768, // 画面幅が768px以下になったら
      settings: {
        slidesToShow: 1,   // 1分割（1枚表示）に変える
        slidesToScroll: 1  // 1枚ずつスクロールする
      }
    }
  ]
  });
  
