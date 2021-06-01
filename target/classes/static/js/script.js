const toggleSidebar = () => {

    if($('.sidebar').is(":visible")){

        $(".sidebar").css("display","none")
        $(".content").css("margin-left","0%");
    }else {

        $(".sidebar").css("display","block")
        $(".content").css("margin-left","20%");
    }
};

$(document).ready(function(){
        $('.telephone').inputmask('(999)-999-9999');
});

const search = () => {
    console.log("wyszukiwanie...");

    let query = $ ("#search-input").val();
    console.log(query);

    if(query == ''){

        $(".search-result").hide();

    } else {

        console.log(query);

        

        $(".search-result").show();

    }
}
