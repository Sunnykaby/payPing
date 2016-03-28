//package com.kami.tools;
//
//public class test {
//	%分段准备工作，主要获取时间序列，速度序列，点差距离序列，以及与各个窗口中rest_pos的距离序列
//	%其中包括，获取每个窗口的rest_pos点
//	%输入：经过java程序计算得到的位置pos三轴数据，还有时间数据，pos_t,pos_x,pos_y,pos_z
//	%输出：Distance_from_RestPos，Time_array，Velocity_array
//	i = 0;
//	sliding_time_window = 7000;%滑动窗口，表示时间
//	velocity_thredshold = 0.5;%低速过滤阀值
//	temp_x = 0;
//	temp_y = 0;
//	temp_z = 0; 
//	temp_count = 0;
//	current_rest_pos = 1;
//	%创建相关序列。matlab的矩阵都是从1开始的，这里将第一个值设为0
//	Time_array = [];%时间序列（从0开始）
//	Time_array(1) = 0; 
//	Distance_array =  [];%前后两点之间的距离序列
//	Distance_array(1) = 0;
//	Velocity_array = [];%速度序列（用的平均速度，不是用acc计算的瞬时速度）
//	Velocity_array(1) = 0;
//	Distance_from_RestPos = [];%通过计算的reat_pos获得的距离序列a
//	Distance_from_RestPos(1) = 0;
//	%默认所有的序列初始值为0
//
//	matrix_size = numel(pos_t);%获取该组数据的数据量
//	rest_pos_Matrix = zeros(ceil((pos_t(matrix_size) - pos_t(1))/sliding_time_window),3); %保存每个窗口内的rest_pos的坐标点，三维
//	rest_pos_count = 0;%保存该组实验内的rest_pos数量
//	low_velocity_pos = false(1,matrix_size);%保存低速点的位置（低速点上位true），用来做图像显示。
//
//	for i= 1:(matrix_size-1)
//	    diff_time = pos_t(i+1) - pos_t(i);
//	    Time_array(i+1) = Time_array(i) + diff_time;%计算时间序列
//	    diff_distance = sqrt((pos_x(i+1) - pos_x(i))^2 + (pos_y(i+1) - pos_y(i))^2 + (pos_z(i+1) - pos_z(i))^2);
//	    Distance_array(i+1) = diff_distance;%计算距离差序列
//	    Velocity_array(i+1) = (diff_distance/diff_time) * 1000;%计算平均速度序列
//	    
//	    %是否在窗口里
//	    if rem(Time_array(i+1),sliding_time_window) < rem(Time_array(i),sliding_time_window)
//	        %如果是新的窗口段
//	        rest_pos_count = rest_pos_count + 1;
//	        %将上一个窗口的rest_pos保存
//	        rest_pos_Matrix(rest_pos_count,1) = temp_x / temp_count;
//	        rest_pos_Matrix(rest_pos_count,2) = temp_y / temp_count;
//	        rest_pos_Matrix(rest_pos_count,3) = temp_z / temp_count;
//	        %重新开始新的窗口的rest_pos计算
//	        temp_x = 0;
//	        temp_y = 0;
//	        temp_z = 0;
//	        temp_count = 0;
//	    end
//	    %低速点获取
//	    if Velocity_array(i+1) < velocity_thredshold  && pos_z(i+1) < 0
//	        temp_x = temp_x + pos_x(i+1);
//	        temp_y = temp_y + pos_y(i+1);
//	        temp_z = temp_z + pos_z(i+1);
//	        temp_count = temp_count + 1;
//	        low_velocity_pos(i+1) = 1;
//	    end
//	end
//	%将最后一个窗口的自然点找到
//	if rest_pos_count < ceil((pos_t(matrix_size) - pos_t(1))/sliding_time_window)
//	    %如果自然点数量不足总体窗口数
//	    rest_pos_count = rest_pos_count + 1;
//	    rest_pos_Matrix(rest_pos_count,1) = temp_x / temp_count;
//	    rest_pos_Matrix(rest_pos_count,2) = temp_y / temp_count;
//	    rest_pos_Matrix(rest_pos_count,3) = temp_z / temp_count;
//	    temp_x = 0;
//	    temp_y = 0;
//	    temp_z = 0;
//	    temp_count = 0;
//	end
//
//	for i = 1:(matrix_size-1)
//	   if rem(Time_array(i+1),sliding_time_window) < rem(Time_array(i),sliding_time_window)
//	       %如果是新的窗口，将当前rest_pos换为新窗口的自然点
//	        current_rest_pos = current_rest_pos + 1;
//	   end 
//	   %计算相对rest_pos距离
//	   Distance_from_RestPos(i+1) = sqrt((pos_x(i+1) - rest_pos_Matrix(current_rest_pos,1))^2 + (pos_y(i+1) - rest_pos_Matrix(current_rest_pos,2))^2 + (pos_z(i+1) - rest_pos_Matrix(current_rest_pos,3))^2);
//	end
//
//
//	% %显示低速点
//	% x= 1:matrix_size;
//	% y = Velocity_array;
//	% plot(x,y);
//	% hold on;
//	% plot(x(low_velocity_pos),y(low_velocity_pos),'ro');
//
//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//	%自拟的peak/trough探测算法
//	%输入数据：与rest_pos的距离序列，Distance_from_RestPos
//	%输出：self_peak_detection，self_trough_detection
//	spliding_window_size = 5;%滑动窗口长度，表示包含点的个数
//	temp_pos = zeros(1,spliding_window_size);%保存临时点（窗口内）
//	size_s = numel(Distance_from_RestPos);%一共点的个数
//	peaks_pos = false(1,size_s);%保存获取到的Peak候选点位置
//	peak_pos = false(1,size_s);%保存筛选后的Peak点位置
//	troughs_pos = false(1,size_s);%保存获取到的Trough候选点位置
//	trough_pos = false(1,size_s);%保存筛选后的Trough点位置
//	temp_look = zeros(ceil(size_s/spliding_window_size),3); %r保存每个窗口内的计算结果，var，avg和diff
//
//	peak_count = 0;%peak点计数
//	trough_count = 0;%trough点计数
//
//	variance_thred = 0.1;%计算Peak和trough拐点的方差阈值
//	avg_thred = 1;%计算Peak和trough拐点的均值阈值
//	diff_thred = 1;%计算Peak和trough拐点的差分和阈值
//
//
//	for i = 1:spliding_window_size:size_s 
//	    %i是开始的index
//	    if floor((i-1)/spliding_window_size)~=floor(size_s/spliding_window_size)
//	        for j = 1:spliding_window_size
//	        temp_pos(j) = Distance_from_RestPos(i + j - 1);
//	        end
//	    
//	    temp_var = var(temp_pos)*4;%计算方差，为了图像和结果更具意义，扩大4倍
//	    temp_avg = mean(temp_pos);%计算均值
//	    temp_diff = sum(diff(temp_pos));%计算差分和
//	    temp_look(ceil(i/spliding_window_size),1) = temp_var;
//	    temp_look(ceil(i/spliding_window_size),2) = temp_avg;
//	    temp_look(ceil(i/spliding_window_size),3) = temp_diff;
//	    
//	    %经验阀值
//	    if temp_avg < 0.2 && temp_var < 0.05 
//	       for j = 1:spliding_window_size
//	        troughs_pos(i+j -1) = 1;%候选trough
//	       end
//	    else if temp_avg > 1.2 && temp_var < 0.1
//	            for j = 1:spliding_window_size
//	        peaks_pos(i+j -1) = 1;%候选peak
//	            end 
//	        end
//	    end
//	    end
//	end
//	%将候选点转为真实点
//	for i = 1:size_s
//	    if peaks_pos(i) == 0
//	        if peak_count < 0
//	            peak_pos(i-2)=1;
//	        end
//	        peak_count= 1;
//	    else 
//	        if peak_count > 0
//	            peak_pos(i+2) = 1;
//	        end
//	        peak_count = -1;
//	    end
//	    
//	     if troughs_pos(i) == 0
//	        if trough_count < 0
//	            trough_pos(i-2)=1;
//	        end
//	        trough_count= 1;
//	    else 
//	        if trough_count > 0
//	            trough_pos(i+2) = 1; 
//	        end
//	        trough_count = -1;
//	    end
//	end
//	%将peak和trough点保存至自定义探测算法的名称
//	self_peak_detection = peak_pos;
//	self_trough_detection = trough_pos;
//
//	%打印peak和trough点，自己探测到
//	% x= 1:size_s;
//	% y = Distance_from_RestPos;
//	% plot(x,y);
//	% hold on;
//	% plot(x(peak_pos),y(peak_pos),'ko');
//	% plot(x(trough_pos),y(trough_pos),'mo');
//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//	%经典peak/trough探测算法
//	%输入数据：与rest_pos的距离序列。Distance_from_RestPos
//	%输出数据：org_peak_detection，org_trough_detection
//	size_s = numel(Distance_from_RestPos);
//	peaks_pos = false(1,size_s);
//	troughs_pos=false(1,size_s);
//	%使用peakfinder函数来得到经典peak和trough
//	[peakLoc] = peakfinder(Distance_from_RestPos,0.01,1,1);
//	[troughLoc] = peakfinder(Distance_from_RestPos, 0.1, 1, -1);
//
//	temp_collum = numel(peakLoc);
//	for i = 1:temp_collum
//	    peaks_pos(peakLoc(i)) = 1;
//	end
//	temp_collum = numel(troughLoc);
//	for j = 1:temp_collum
//	    troughs_pos(troughLoc(j)) = 1;
//	end
//	%将经典探测算法得到的点用org表示
//	org_peak_detection = peaks_pos;
//	org_trough_detection = troughs_pos;
//	% x= 1:size_s;
//	% y = Distance_from_RestPos;
//	% plot(x,y);
//	% hold on;
//	% plot(x(peaks_pos),y(peaks_pos),'ro');
//	% plot(x(troughs_pos),y(troughs_pos),'go');
//	%
//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//	%结合传统和self探测算法，获得更精准的peak和trough点
//	%输入数据：与rest_pos的距离序列Distance_from_RestPos，org_peak_detection,org_trough_detection,self_peak_detection,self_trough_detection
//	%输出数据：self_peak_detection，self_trough_detection（修正过后）
//	size_s = numel(Distance_from_RestPos);
//	threshold_win = 5;%和自己探测算法里的窗口长度一致
//	for i = 1:size_s
//	    if self_peak_detection(i) == 1
//	        if i <= threshold_win
//	            for j = 1:i-1
//	                if org_peak_detection(j) == 1
//	                    self_peak_detection(i) = 0;
//	                    self_peak_detection(j) = 1;
//	                    break;
//	                end
//	            end
//	        elseif (size_s - i) <= threshold_win
//	            for j = i+1:size_s
//	                if org_peak_detection(j) == 1
//	                    self_peak_detection(i) = 0;
//	                    self_peak_detection(j) = 1;
//	                    break;
//	                end
//	            
//	            end
//	        else 
//	            for j = 1:threshold_win
//	                if org_peak_detection(i+j) == 1
//	                    self_peak_detection(i) = 0;
//	                    self_peak_detection(i+j) = 1;
//	                    break;
//	                end
//	                 if org_peak_detection(i-j) == 1
//	                    self_peak_detection(i) = 0;
//	                    self_peak_detection(i-j) = 1;
//	                 end
//	            end      
//	        end
//	    end
//	    
//	     if self_trough_detection(i) == 1
//	        if i <= threshold_win
//	            for j = 1:i-1
//	                if org_trough_detection(j) == 1
//	                    self_trough_detection(i) = 0;
//	                    self_trough_detection(j) = 1;
//	                    break;
//	                end
//	            end
//	        elseif (size_s - i) <= threshold_win
//	            for j = i+1:size_s
//	                if org_trough_detection(j) == 1
//	                    self_trough_detection(i) = 0;
//	                    self_trough_detection(j) = 1;
//	                    break;
//	                end
//	            
//	            end
//	        else 
//	            for j = 1:threshold_win
//	                if org_trough_detection(i+j) == 1
//	                    self_trough_detection(i) = 0;
//	                    self_trough_detection(i+j) = 1;
//	                    break;
//	                end
//	                 if org_trough_detection(i-j) == 1
//	                    self_trough_detection(i) = 0;
//	                    self_trough_detection(i-j) = 1;
//	                 end
//	            end      
//	        end
//	    end
//	end
//	%查看新的修正点
//	% x= 1:size_s;
//	% y = Distance_from_RestPos;
//	% plot(x,y);
//	% hold on;
//	% plot(x(self_peak_detection),y(self_peak_detection),'rs');
//	% plot(x(self_trough_detection),y(self_trough_detection),'gs')
//	% line_y = linspace(2,2,size_s);
//	% stem(x(self_trough_detection),line_y(self_trough_detection),'k');
//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//	%size_s = numel(Distance_from_RestPos);
//	%function find0110
//	%利用找到的peak和trough点，得到segment。
//	%输入数据：self_peak_detection,self_trough_detection
//	%输出数据：segment_pos
//	%trough_a -2
//	%peak_a -1
//	%peak_b 1
//	%trough_b 2
//	%segment -2 -1 1 2
//	segment_pos = zeros(1,size_s);
//	start_trough = 0;
//	start_peak = 0;
//	end_peak = 0;
//	end_trough = 0;
//	segment_peaks = self_peak_detection;
//	segment_troughs = self_trough_detection;
//	% use the number of point, not the time between both
//	peak_peak_min = 10;%需要进一步考量5,10
//	peak_peak_max = 50;%50
//	peak_trough_min = 1;
//	peak_trough_max = 15;%需要进一步考量这个阀值的效果19,15
//
//	for i = 1:size_s
//	    if segment_troughs(i) == 1
//	        if start_peak == 0 
//	            segment_pos(i) = -2;
//	            start_trough = 1;
//	        else if end_peak ~= 0 && start_peak ~= 0
//	                segment_pos(i) = 2;
//	                end_trough = 0;
//	                start_trough = 0;
//	                start_peak = 0;
//	                end_peak = 0;
//	            end
//	        end
//	    end
//	    if segment_peaks(i) == 1
//	        if start_trough == 1 && start_peak == 0
//	            segment_pos(i) = -1;
//	           start_peak = 1;
//	        else if start_peak == 1 && end_trough == 0
//	                segment_pos(i) = 1;
//	                end_peak = 1;
//	            end
//	        end
//	    end            
//	end
//
//	last_pos1 = 1;
//	last_pos2 = 1;
//	last_pos3 = 1;
//	last_pos4 = 1;
//
//	current_flag1 = 0;
//	current_flag2 = 0;
//	current_flag3 = 0;
//	current_flag4 = 0;
//	current_count = 0;
//	%last_start_pos = 0;
//
//	for i = 1:size_s
//	    if(segment_pos(i) ~= 0)
//	        switch segment_pos(i)
//	            case -2
//	                if current_count ~= 0
//	                    segment_pos(last_pos1) = 0;
//	                    segment_pos(last_pos2) = 0;
//	                    segment_pos(last_pos3) = 0;
//	                end
//	                current_count =1;
//	                last_pos1 = i;
//	                %break;
//	            case -1
//	               if current_count ~= 1 || (i-last_pos1) < peak_trough_min || (i-last_pos1) > peak_trough_max
//	                    segment_pos(last_pos1) = 0;
//	                    segment_pos(last_pos2) = 0;
//	                    segment_pos(last_pos3) = 0;
//	                    segment_pos(i) = 0;
//	                    current_count = 0;
//	               else
//	                    current_count = 2;
//	                    last_pos2 = i;
//	               end
//	                %break;
//	            case 1
//	                 if current_count ~= 2 ||(i-last_pos2) < peak_peak_min || (i-last_pos2) > peak_peak_max
//	                    segment_pos(last_pos1) = 0;
//	                    segment_pos(last_pos2) = 0;
//	                    segment_pos(last_pos3) = 0;
//	                    segment_pos(i) = 0;
//	                    current_count = 0;
//	                 else
//	                    current_count = 3;
//	                    last_pos3 = i;
//	                 end
//	               % break;
//	            case 2
//	                 if current_count ~= 3 ||(i-last_pos3) < peak_trough_min || (i-last_pos3) > peak_trough_max
//	                    segment_pos(last_pos1) = 0;
//	                    segment_pos(last_pos2) = 0;
//	                    segment_pos(last_pos3) = 0;
//	                    segment_pos(i) = 0;
//	                    current_count = 0;
//	                 else
//	                    current_count = 0;
//	                    last_pos1 = 1;
//	                    last_pos2 = 1;
//	                    last_pos3 = 1;
//	                 end
//	               % break;
//	        end
//	    end
//	end
//
//	a
//	if last_pos1 ~= 0
//	segment_pos(last_pos1) = 0;
//	end
//	if last_pos2 ~= 0
//	segment_pos(last_pos2) = 0;
//	end
//	if last_pos3 ~= 0
//	segment_pos(last_pos3) = 0;
//	end
//
//	x= 1:size_s;
//	y = segment_pos;
//	plot(x,y);
//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//	%利用rotation原始数据生成角速度，roll
//	%rotation_cos = zeros(1,size_s);
//	%输入：rotation三轴数据
//	%输出：yaw，roll，pitch，roll角速度Velocity_roll_array
//	rotation_cos = sqrt(1- rotation_x.^2 - rotation_y.^2 - rotation_z.^2);
//	rotation = [rotation_cos rotation_x rotation_y rotation_z];
//	[yaw, pitch, roll] = quat2angle(rotation);
//	Velocity_roll_array = zeros(1,size_s);
//
//	for i= 1:(size_s - 1)
//	    diff_time = rotation_t(i+1) - rotation_t(i);
//	    diff_roll = roll(i+1) - roll(i);
//	    Velocity_roll_array(i+1) = (diff_roll/diff_time) * 1000;
//	end
//	    
//}
