%% I am called puzzle solver at work. I help my team members debug and also love to solve the problems come up with them. 
%%% Followings are some examples I solve at work.

%% Puzzler 1: The proportionalizer puzzle
%%% Requirement: 
%%% 1. For normal stocks, the weights are limited to 6%
%%% 2. For ETF or Mutual Funds, the weights are limited to 10%
%%% 3. When these two changes, other "normal" weights need to be adjusted as well, so they add up to 1.
%%% 4. The newly-weighted portions need to be rechecked, and if there are newly-adjusted weights greater than the boudnary, you need to reallocate.
%%% 5. How to do it one time?

clear
close
clc

%% read the data
aa = readtable('O_weight.csv'); 
w = aa.Var1;
amount = sum(aa.Var2);
ticker = aa.Var3;

%% The allocation process

ETF_or_FUND = ["APO","ARI","ASHIX","DFJ","DSENX","DSUM","DVY","FCNTX","FOF","MKL","MTUM","NTTYY","REM","VIG","VPACX","VYM","IWD","SPY"];
EorF = contains(ticker,ETF_or_FUND);

th_up = EorF * 0.1 + (1 - EorF) * 0.06;
th_lo = 0.005 * ones(size(EorF));

new_w = w;
position = [];
counter = 0;
    while sum(new_w > th_up | new_w< th_lo) > 0 && counter<100
        for i = 1:length(th_up)
        if new_w(i)> th_up(i)
            new_w(i) = th_up(i);
            position(i) = i;
        elseif new_w(i) < th_lo(i)
            new_w(i) = 0;
            position(i) = i;
        end
        end   
po = unique(position);
po = po(2:end);
    to = [1:length(w)];
    to(po) = [];
    rest_w = sum(new_w(to));
new_w(to) = ((1-sum(new_w(po)))*new_w(to))/sum(new_w(to));
counter = counter +1;
    end
    new_amount = new_w * amount(end);

%% Puzzler 2: How is the FALL compared to MAR
%%% Background knowledge:
%%% MAR is a existed terminology, it is a measurement of returns adjusted for risk that can be used to compare the 
%%% performance of commodity trading advisors, hedge funds and trading strategies. 
%%% It is computed by Smooth return / Maximum Drawdown
%%% And it triggers my cuorisity that what if I only care about the worst case? So I decide to make an experiment to figure it out.
%%% I took two periods instead of all-the periods draw-down.

clear
close
clc
aa = readtable('ad3yrdata_0910.csv'); 
Price_raw = (aa.price_adjusted);
symbols_raw = aa.ticker;
Date_raw = aa.ref_date;       

%% form the new table

Table_1 = table(symbols_raw,Date_raw,Price_raw);
Table_1.Properties.VariableNames = [{'symbol'},{'date'},{'price'}];

%% Select the time period
stickers = ["ACBFF","AMAT","AMZN","APO","ARI","ASHIX","ATO","AWK","BABA","DFJ","DSENX","DSUM","DVY","FAF","FB","FCNTX","FOF","GOOGL","GWPH","MKL","MTUM","MWATX","NOW","NTTYY","QAI","REM","RIO","RTN","TCEHY","TRU","TSM","TXN","TTAC","UNH","VIG","VPACX","VYM","IWD","SPY"];

peak1 = Table_1(find(Date_raw >= datetime(2015,12,14) & Date_raw <= datetime(2015,12,30)),:);
bottom1 = Table_1(find(Date_raw >= datetime(2016,1,15) & Date_raw <= datetime(2016,1,30)),:);
peak2 = Table_1(find(Date_raw >= datetime(2018,1,15) & Date_raw <= datetime(2018,1,26)),:);
bottom2 = Table_1(find(Date_raw >= datetime(2018,2,5) & Date_raw <= datetime(2018,2,16)),:);


% APO ARI ATAX AVGO DFJ DSENX FAF FCNTX GLD HEDJ
% INTC LYB MTUM NTTYY RYAAY UNH VGHCX
% separate the stocks
stock_ts = {};
price_panel = [];

for counter1 = 1:length(stickers)
    symbol_temp = stickers(counter1);                                       % return the unrepeated stickers
    position_temp = find(string(peak1.symbol) == symbol_temp);               % return the positions of every stickers
    
    ts_temp = peak1(position_temp,2:end);
    price_temp = ts_temp.price;
   price_temp = str2double(price_temp);
        
    eval([symbol_temp{1},'_ts=ts_temp;']);
    %eval([symbol_temp{1},'=price_temp;']);
    
    stock_ts{counter1} = ts_temp;
    price_panel(1:length(price_temp),counter1) = price_temp;
end
date_panel = ts_temp.date;
clear -regexp temp;
clear counter1 ;

price_table_peak1 = table(date_panel,price_panel);
price_table_peak1.Properties.VariableNames = [{'date'},{'price'}];

% form a bottom1 table
stock_ts = {};
price_panel = [];

for counter1 = 1:length(stickers)
    symbol_temp = stickers(counter1);                                       % return the unrepeated stickers
    position_temp = find(string(bottom1.symbol) == symbol_temp);               % return the positions of every stickers
    
    ts_temp = bottom1(position_temp,2:end);
    price_temp = ts_temp.price;
   price_temp = str2double(price_temp);
        
    eval([symbol_temp{1},'_ts=ts_temp;']);
    %eval([symbol_temp{1},'=price_temp;']);
    
    stock_ts{counter1} = ts_temp;
    price_panel(1:length(price_temp),counter1) = price_temp;
end
date_panel = ts_temp.date;
clear -regexp temp;
clear counter1 ;

price_table_bottom1 = table(date_panel,price_panel);
price_table_bottom1.Properties.VariableNames = [{'date'},{'price'}];

% form a bottom2 table
stock_ts = {};
price_panel = [];

for counter1 = 1:length(stickers)
    symbol_temp = stickers(counter1);                                       % return the unrepeated stickers
    position_temp = find(string(bottom2.symbol) == symbol_temp);               % return the positions of every stickers
    
    ts_temp = bottom2(position_temp,2:end);
    price_temp = ts_temp.price;
   price_temp = str2double(price_temp);
        
    eval([symbol_temp{1},'_ts=ts_temp;']);
    %eval([symbol_temp{1},'=price_temp;']);
    
    stock_ts{counter1} = ts_temp;
    price_panel(1:length(price_temp),counter1) = price_temp;
end
date_panel = ts_temp.date;
clear -regexp temp;
clear counter1 ;

price_table_bottom2 = table(date_panel,price_panel);
price_table_bottom2.Properties.VariableNames = [{'date'},{'price'}];

% form a peak2 table
% separate the stocks
stock_ts = {};
price_panel = [];

for counter1 = 1:length(stickers)
    symbol_temp = stickers(counter1);                                       % return the unrepeated stickers
    position_temp = find(string(peak2.symbol) == symbol_temp);               % return the positions of every stickers
    
    ts_temp = peak2(position_temp,2:end);
    price_temp = ts_temp.price;
   price_temp = str2double(price_temp);
        
    eval([symbol_temp{1},'_ts=ts_temp;']);
    %eval([symbol_temp{1},'=price_temp;']);
    
    stock_ts{counter1} = ts_temp;
    price_panel(1:length(price_temp),counter1) = price_temp;
end
date_panel = ts_temp.date;
clear -regexp temp;
clear counter1 ;

price_table_peak2 = table(date_panel,price_panel);
price_table_peak2.Properties.VariableNames = [{'date'},{'price'}];


%% Find the peak and bottom for each stock for the two periods
A_peak = table2array(price_table_peak1(:,2));
A_bottom = table2array(price_table_bottom1(:,2));
A_peak2 = table2array(price_table_peak2(:,2));
A_bottom2 = table2array(price_table_bottom2(:,2));

Fall = [];
for i = 1:size(A_peak,2)
    Fall(i,1) = max(A_peak(:,i));
    Fall(i,2) = min(A_bottom(:,i));
    Fall(i,4) = max(A_peak2(:,i));
    Fall(i,5) = min(A_bottom2(:,i));
    
end


Fall(:,3) = Fall(:,2)./Fall(:,1) -1;
Fall(:,6) = Fall(:,5)./Fall(:,4) -1;





