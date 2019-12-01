$(document).ready(function () {
    $('#Student').select2({        
 // Initial value that is selected if no other selection is made.
 placeholder: 'Enter Student ',
 // multiple selection enabled.
 multiple: true,
 ajax: {
        //How long the user has to pause their typing before sending the next request
        quietMillis: 150,
        // The url of the json service
        url: 'StudentServlet',
        dataType: 'json',
        // term contains the search item
        data: function (term, page) {
          return {
          aux_variable : 'Testing',                    
           term: term
          };
        },
        results: function (data) {
          return { 
           results: data  
          };
        }
 }
    });
});