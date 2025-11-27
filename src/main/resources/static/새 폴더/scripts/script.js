$(document).ready(function(){

    // 햄버거버튼
    $('.ham').click(function(){
        if ($('.header').hasClass('on')) {
            $('.header').removeClass('on');
            $('.depth2').css('display','block');
        }else{
            $('.header').addClass('on');
            $('header').addClass('on');
            $('.depth2').css('display','none');
        }
    });
   
   /*
    $('.nav').hover(function() {
        if ($('.header').hasClass('on')) {
            $('.nav_bg').css('display','block');
            $('.depth2').css('display','block');
        }
    }, function(){
        if ($('.header').hasClass('on')) {
            $('.nav_bg').css('display','none');
            $('.depth2').css('display','none');
        }
    });

    */
    
    
    $('.nav_bg').hover(function() {
        if ($('.header').hasClass('on')) {
            $('.nav_bg').css('display','block');
            $('.depth2').css('display','block');
        }
    }, function(){
        if ($('.header').hasClass('on')) {
            $('.nav_bg').css('display','none');
            $('.depth2').css('display','none');
        }
    });    


    $('.toggle').click(function(){
        $('.toggle_box').slideToggle();
        // $('.toggle').addClass('on');
        

        if ($('.toggle').hasClass('on')) {
            $('.toggle').removeClass('on');
            $('.part_hide_grid').removeClass('on');
        }else{
            $('.toggle').addClass('on');
            $('toggle').addClass('on');
            $('.part_hide_grid').addClass('on');
        }        
    });      

   

    // 탑버튼
    $('#etc').hide();
    $(window).scroll(function() {

        if ($(this).scrollTop() > 2000) {
            $('#etc').fadeIn();
        } else {
            $('#etc').fadeOut();
        }
    });
    $("#top_btn").click(function() {
        $('html, body').animate({
            scrollTop : 0
        }, 400);
        return false;
    });

    // 모달창 켜기1
    $('.modal_step_open').click(function(){
        var modal_step = $(this).attr('modal_step');

        $('.modal_step').addClass('hidden'); // 이전 모달창을 숨김
        
        $(this).addClass('current');
        $("."+ "modal_step" +modal_step).removeClass('hidden');
    })

    // 모달창 켜기1
    $('.modal_step_open2').click(function(){
        var modal_step = $(this).attr('modal_step');
        
        $(this).addClass('current');
        $("."+ "modal_step" +modal_step).removeClass('hidden');
    })


    // 모달창 켜기2
    $('.modal_open').on("click", function() {
        $(this).next().removeClass("hidden");
    });

    // 모달창 끄기
    $('.cancerButton').on("click", function() {
        if ($(this).closest(".modal")) {
            $(this).closest(".modal").addClass("hidden");
        }
        else {
            $(this).closest(".modal").addClass("hidden");
        }
    });

    // 모든 모달창 끄기
    $('.allModalClose').on("click", function() {
        $('.modal').addClass("hidden");
    });

    // 첫번째 모달창 끄기
    $('.firstModalClose2').on("click", function() {
        $(this).closest('.modal').addClass("hidden");
        $(this).closest('.modal').next('.modal').removeClass("hidden");
    });   
});
