# bidding-server

## Overview

Bidding Server is a custom, high performance, auction server for this online car auction company's web based auctions.
Biding Server manages all the aspect of the whole auction process, from creating an auction to announcing a winner.

The server allows following capabilities to their web-based auction clients:

✅ Only admin must be able to create auction <br />
&ensp;&ensp;&ensp;&ensp;✅ If there already exists a running auction for the given item then auction will not be created<br />
✅ user can bid on a running auction<br />
&ensp;&ensp;&ensp;&ensp;✅ Only logged in users are allowed to place bid<br />
&ensp;&ensp;&ensp;&ensp;✅ Accepted or rejected all user bids must be recorded <br />
&ensp;&ensp;&ensp;&ensp;✅ User must not be able to bid on an item which has no running auction or the auction is expired<br />
✅ user must be able to get the list of running auction<br />
&ensp;&ensp;&ensp;&ensp;✅ If user gave an invalid auction status system must return null response<br />
✅ Bid Accept Scenarios<br />
&ensp;&ensp;&ensp;&ensp;✅ If there exists no bid against the item and bid amount greater than or equal to base price<br />
&ensp;&ensp;&ensp;&ensp;✅ If there exists a bid against the item and upcoming bid must be greater than or equal to sum of highest bid and step rate<br />
✅ Bid Rejected Scenarios<br />
&ensp;&ensp;&ensp;&ensp;✅ If there exists no bid against the item and bid amount is less than base price<br />
&ensp;&ensp;&ensp;&ensp;✅ If there exists a bid against the item and upcoming bid is less than the sum of highest bid and step rate<br />
✅ Auction not found scenarios<br />
&ensp;&ensp;&ensp;&ensp;✅ If auction is expired<br />
&ensp;&ensp;&ensp;&ensp;✅ If auction is null<br />
&ensp;&ensp;&ensp;&ensp;✅ If item does not have running auction<br />
✅ As soon as the auction is over winner receives email<br />
✅ Auction automatically gets over once duration completes<br />
