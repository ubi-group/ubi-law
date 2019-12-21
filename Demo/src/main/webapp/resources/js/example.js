 $(document).ready(function() {
    $("#search").autocomplete({     
          source : function(request, response) {
               $.ajax({
                    url : "Controller",
                    type : "GET",
                    data : {
                           term : request.term
                    },
                    dataType : "json",
                    success : function(data) {
                          response(data);
                    }
             });
          },
          select: function( event, ui ) {
             $("#search").val(ui.item.value);
             return false;
          }
    });
});  
    