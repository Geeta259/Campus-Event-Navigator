
const toggleSidebar=()=>{
	if($(".sidebar").is(":visible"))
	{
	    $(".sidebar").css("display","none");
	    $(".content").css("margin-left","0%");	
	}
	else
	{
		 $(".sidebar").css("display","block");
	    $(".content").css("margin-left","20%");
	}
};


function deleteEvent(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this event?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-event/"+id;
	    
	  } else {
	    swal("Your event is safe!");
	  }
	});
}


function deleteSponsor(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this sponsor?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-sponsor/"+id;
	    
	  } else {
	    swal("Event sponsor is safe!");
	  }
	});
}


function deleteCategory(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this category?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-category/"+id;
	    
	  } else {
	    swal("Event category is safe!");
	  }
	});
}


function deleteCoordinator(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this coordinator?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-coordinator/"+id;
	    
	  } else {
	    swal("Event coordinator is safe!");
	  }
	});
}
function acceptUser(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to add this user in application?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willAccept) => {
	  if (willAccept) {
	     window.location="/member/accept-user/"+id;
	    
	  } else {
	    swal("User not verify yet...");
	  }
	});
}
function deleteUser(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this user?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-user/"+id;
	    
	  } else {
	    swal("User request is safe!");
	  }
	});
}

function deleteContact(id)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this contact?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-contact/"+id;
	    
	  } else {
	    swal("Event contacts is safe!");
	  }
	});
}
function approvedBooking(eid,uid)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to approved this booking?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/approved-booking/"+eid+"/"+uid;
	    
	  } else {
	    swal("This event booking still pending..");
	  }
	});
}
function rejectBooking(eid,uid)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to reject this booking?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/reject-booking/"+eid+"/"+uid;
	    
	  } else {
	    swal("This event booking still pending..");
	  }
	});
}

function deleteBooking(eid,uid)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this booking?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-booking/"+eid+"/"+uid;
	    
	  } else {
	    swal("This event booking rejected!!");
	  }
	});
}
function deleteFeedback(fid)
{
	swal({
	  title: "Are you sure?",
	  text: "You want to delete this feedback?",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
	     window.location="/member/delete-feedback/"+fid;
	    
	  } else {
	    swal("This feedback  is  safe!!");
	  }
	});
}

