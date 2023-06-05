# bidding-server

## Overview

Bidding Server is a custom, high performance, auction server for this online car auction company's web based auctions.
Biding Server manages all the aspect of the whole auction process, from creating an auction to announcing a winner.

The server allows following capabilities to their web-based auction clients:

- [x] Only admin must be able to create auction <br />
    - [x] If there already exists a running auction for the given item then auction will not be created<br />
- [x] user can bid on a running auction<br />
    - [x] Only logged in users are allowed to place bid<br />
    - [x] Accepted or rejected all user bids must be recorded <br />
    - [x] User must not be able to bid on an item which has no running auction or the auction is expired<br />
- [x] user must be able to get the list of running auction<br />
    - [x] If user gave an invalid auction status system must return null response<br />
- [x] Bid Accept Scenarios<br />
    - [x] If there exists no bid against the item and bid amount greater than or equal to base price<br />
    - [x] If there exists a bid against the item and upcoming bid must be greater than or equal to sum of highest bid and step rate<br />
- [x] Bid Rejected Scenarios<br />
    - [x] If there exists no bid against the item and bid amount is less than base price<br />
    - [x] If there exists a bid against the item and upcoming bid is less than the sum of highest bid and step rate<br />
- [x] Auction not found scenarios<br />
    - [x] If auction is expired<br />
    - [x] If auction is null<br />
    - [x] If item does not have running auction<br />
- [x] As soon as the auction is over winner receives email<br />
- [x] Auction automatically gets over once duration completes<br />