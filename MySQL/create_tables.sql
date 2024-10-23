-- Create Time: 2024/10/22
-- Author: 高涵宸 (CrazyApple)
-- studentID:22009200189
-- Description: 创建数据库的全部表格和相关需要使用的触发器，便于数据操作
-- 整个程序的数据库设计是一个旅游预定系统，包括航班、宾馆、巴士的预定，以及顾客的信息
-- 顾客可以预定航班、宾馆、巴士，每个预定都会在预定表格中有一条记录，同时会更新航班、宾馆、巴士的剩余数量
-- 简化客户的旅行航线，巴士只能在宾馆所在的城市使用，航班只能在两个城市之间使用

-- 创建航班的表格
-- flightNum是航班的编号
-- price是航班的价格
-- numSeats是航班的座位数量
-- numAvail是航班的剩余座位数量
-- FromCity是航班的出发城市
-- ArivCity是航班的到达城市
create table flights(
    flightNum char(30) not null,
    price int,
    numSeats int not null,
    numAvail int,
    FromCity char(30),
    ArivCity char(30),
    primary key(flightNum),
    constraint check_flight_num check(numAvail >= 0 and numAvail <= numSeats)
);

-- 创建宾馆的表格
-- location是宾馆的位置
-- price是宾馆的价格
-- numRooms是宾馆的房间数量
-- numAvail是宾馆的剩余房间数量
create table hotels(
    location char(30) not null,
    price int,
    numRooms int not null,
    numAvail int,
    primary key(location),
    constraint check_hotel_num check(numAvail >= 0 and numAvail <= numRooms)
);

-- 创建巴士的表格
-- location是巴士的位置
-- price是巴士的价格
-- numBus是巴士的数量
-- numAvail是巴士的剩余数量
create table bus(
    location char(30) not null,
    price int,
    numBus int not null ,
    numAvail int,
    constraint check_bus_num check(numAvail >= 0 and numAvail <= numBus)
);

-- 创建顾客的表格
-- custName是主键，是客户的姓名
-- custID是客户的ID
create table customers(
    custName char(30) not null,
    custID char(30),
    primary key(custName)
);

-- 创建预约信息表格
-- resvKey是主键，是预约的标记
-- resvType是预约的类型，1是预定航班，2是预定宾馆，3是预定巴士
-- custName是预约的顾客
create table reservations(
    custName char(30),
    resvType int,
    -- 确定预定的location或者flightNum来唯一确定一条预约
    resvKey char(30) not null,
    primary key(resvKey),
    foreign key(custName) references customers(custName)
);

-- 创建检查resvType内容是否合法的触发器
create trigger check_resv_type before insert on reservations
for each row
begin
    if new.resvType < 1 or new.resvType > 3 then
        signal sqlstate '45000' set message_text = 'resvType must be 1, 2 or 3';
    end if;
end;

-- 创建检查resvKey内容是否合法的触发器
create trigger check_resv_key before insert on reservations
for each row
begin
    if new.resvType = 1 then
        if not exists(select * from flights where flightNum = new.resvKey) then
            signal sqlstate '45000' set message_text = 'flightNum not exists';
        end if;
    end if;
    if new.resvType = 2 then
        if not exists(select * from hotels where location = new.resvKey) then
            signal sqlstate '45000' set message_text = 'location not exists';
        end if;
    end if;
    if new.resvType = 3 then
        if not exists(select * from bus where location = new.resvKey) then
            signal sqlstate '45000' set message_text = 'location not exists';
        end if;
    end if;
end;

-- 创建更新航班、宾馆、巴士剩余数量的触发器
create trigger reset_avail after insert on reservations
for each row
begin
    if new.resvType = 1 then
        update flights set numAvail = numAvail - 1 where flightNum = new.resvKey;
    end if;
    if new.resvType = 2 then
        update hotels set numAvail = numAvail - 1 where location = new.resvKey;
    end if;
    if new.resvType = 3 then
        update bus set numAvail = numAvail - 1 where location = new.resvKey;
    end if;
end;

create trigger release_avail after delete on reservations
for each row
begin
    if old.resvType = 1 then
        update flights set numAvail = numAvail + 1 where flightNum = old.resvKey;
    end if;
    if old.resvType = 2 then
        update hotels set numAvail = numAvail + 1 where location = old.resvKey;
    end if;
    if old.resvType = 3 then
        update bus set numAvail = numAvail + 1 where location = old.resvKey;
    end if;
end;
