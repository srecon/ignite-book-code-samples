create table exchangerate(
  ratedate 	date,
  usdollar  decimal,
  euro    	decimal,
  gbp		decimal,
  region	text,
  primary key (ratedate, region)
);