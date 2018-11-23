clear
close
clc
aa = readtable('ad1yrdata_0831.csv'); 
Price_raw = (aa.price_adjusted);
symbols_raw = aa.ticker;
Date_raw = aa.ref_date;       

%% The data cleaning
if sum(ismissing(Price_raw))>0
    miss_p = find(ismissing(Price_raw))
    
    % calculate the geometric means
    for changing_point = miss_p
        if symbols_raw(changing_point+1) == symbols_raw(changing_point-1)
            Price_raw(changing_point) = ...
                sqrt(Price_raw(changing_point-1)*Price_raw(changing_point+1));
            
        elseif symbols_raw(changing_point) == symbols_raw(changing_point+1)
            Price_raw(changing_point) = ...
                Price_raw(changing_point+1)^2/Price_raw(changing_point+2);
            
        elseif symbols_raw(changing_point-1) == symbols_raw(changing_point)
            Price_raw(changing_point) = ...
                Price_raw(changing_point-1)^2/Price_raw(changing_point-2);
        else
            fprintf('%.0f is a singular data point\n',miss_p);
            
        end
    end
end

%% form the new table

Table_1 = table(symbols_raw,Date_raw,Price_raw);
Table_1.Properties.VariableNames = [{'symbol'},{'date'},{'price'}];

%% Select the time period
stickers = ["ACBFF","AMAT","AMZN","APO","ARI","ASHIX","ATO","AWK","BABA","DFJ","DSENX","DSUM","DVY","FAF","FB","FCNTX","FOF","GOOGL","GWPH","MKL","MTUM","MWATX","NOW","NTTYY","QAI","REM","RIO","RTN","TCEHY","TRU","TSM","TXN","TTAC","UNH","VIG","VPACX","VYM","IWD","SPY"];

current_t = datetime(2018,08,01);
current_year = year(current_t);
current_mon = month(current_t);
current_day = day(current_t);

estimation_length = 1;  % 1 year
price_ts_es = {};

start_year = current_year - estimation_length;
start_date = datetime([start_year,current_mon,current_day-34]);

Table_estimation = Table_1(find(Date_raw >= (start_date) & Date_raw <= current_t),:);

% APO ARI ATAX AVGO DFJ DSENX FAF FCNTX GLD HEDJ
% INTC LYB MTUM NTTYY RYAAY UNH VGHCX
% separate the stocks
stock_ts = {};
price_panel = [];

for counter1 = 1:length(stickers)
    symbol_temp = stickers(counter1);                                       % return the unrepeated stickers
    position_temp = find(string(Table_estimation.symbol) == symbol_temp);               % return the positions of every stickers
    
    ts_temp = Table_estimation(position_temp,2:end);
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

price_table = table(date_panel,price_panel);
price_table.Properties.VariableNames = [{'date'},{'price'}];

%% formulate the table and deal with the stock splitting
A = table2array(price_table(:,2));

% we need to clean FCNTX, there is a stock split
fcn = A(:,16);
for i = 1:length(fcn)
    if fcn(i) > 100
        fcn(i) = fcn(i) / 10;
    end
end
A(:,16)=fcn;

%% calculate R30 and P30
log_ret=[];
for i=1:36
    log_ret(:,i)=log(A(end-29:end,i))-log(A(end-281:end-252,i));
end

for i=1:size(A,2)
    mva(i)=nanmean(log_ret(1:end,i));
end
mva=mva';

% simple moving average
for i=1:size(A,2)
    mva2(i)=nanmean(A(end-59:end,i));
end
mva2=mva2';

%% get the 1yr semi-month standard deviation
Mon=month(price_table.date);
lag_Mon=lagmatrix(Mon,1);
Mon_turn=find(Mon~=lag_Mon)-1;

for i=2:length(Mon_turn)
    Mid_month(2*(i-1))=(Mon_turn(i-1)+Mon_turn(i))/2;
    Mid_month(2*(i-1)+1)=Mon_turn(i);
end

Mid_month=Mid_month';
Mid_month=Mid_month(4:end,:);
Mid_month=ceil(Mid_month);

% get the semi-monthly data of return and txt
for i=1:length(Mid_month)
    semi_data(:,i)=A(Mid_month(i),:);
end
semi_data=semi_data';

% if end date is not month end, then run the following
semi_data(end+1,:) = price_panel(find(price_table.date == current_t),:);


%calculate mid-monthly return(1-year) and standard deviation
semi_ret = semi_data(2:end,:) ./semi_data(1:end-1,:)-1;
semi_std=nanstd(semi_ret);
ann_std=semi_std.*((24)^0.5);
ann_std=ann_std';


%% calculate 1yr weekly-Friday standard deviation 
Week=week(price_table.date);
lag_Week=lagmatrix(Week,1);
Week_turn=find(Week~=lag_Week)-1;
Week_turn=Week_turn(5:end,:);

% get the weekly data of return and txt

for i=1:length(Week_turn)
    weekly_data(:,i)=A(Week_turn(i),:);
end
weekly_data=weekly_data';

%calculate mid-monthly return(1-year) and standard deviation
weekly_ret = weekly_data(2:end,:) ./weekly_data(1:end-1,:)-1;
weekly_std=nanstd(weekly_ret);
ann_weekly_std=weekly_std.*(52^0.5);
ann_weekly_std=ann_weekly_std';









    