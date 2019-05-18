import React from 'react';
import './App.css';
import './../node_modules/bulma/css/bulma.css';



class MainWindow extends React.Component{
  initial_helper = (max)=>{
    let initial = Array(max);
    for(let i =0;i<max;i++){
      initial[i]=i;
    }
    return initial;
  };
  constructor(props){
    super(props);
    this.state ={
      inMain:this.initial_helper(0),
      outMain:this.initial_helper(32),
      currentPosition:0,
      maxPosition:32,
      currentFault:0,
      memorySize:4,
      strategy:'FIFO'

    }
  }
  onLengthChange = (e)=>{
    this.setState({
      maxPosition:parseInt(e.target.value)
    })
  };
  onLengthSubmit =(e) =>{
    e.preventDefault();
    let new_in = this.initial_helper(0);
    let new_out = this.initial_helper(this.state.maxPosition);
    this.setState({
      inMain:new_in,
      outMain:new_out,
      currentPosition:0,
      currentFault:0,
      memorySize:4,
      strategy:'FIFO'
    })
  };
  OnNextOne =(e) =>{
    let cur_pos = this.state.currentPosition + 1;
    if(cur_pos >= this.state.maxPosition*10){
      cur_pos = this.state.maxPosition*10-1;
    }
    this.setState({
      currentPosition:cur_pos,
    })
    this.virtual_memory_schedual(cur_pos);
  };
  OnPreviousOne =(e) =>{
    let cur_pos = this.state.currentPosition - 1;
    if(cur_pos < 0){
      cur_pos = 0;
    }
    this.setState({
      currentPosition:cur_pos,
    })
    this.virtual_memory_schedual(cur_pos);
  };
  onChange =(e) =>{
    let choosen_pos = e.target.value;
    if(choosen_pos<0){
      choosen_pos=0;
    }else if(choosen_pos>=this.state.maxPosition*10){
      choosen_pos = this.state.maxPosition*10-1;
    }
    this.setState({
      currentPosition:choosen_pos,
    })
  };
  onPositionSubmit= (e)=>{
    e.preventDefault();
    this.virtual_memory_schedual(this.state.currentPosition);
  }
  //#TODO real topic is here! 有没有-》满了吗？-》选一个出去-》拉一个回来
  virtual_memory_schedual = (position)=>{
    console.log('finding #'+position);
    let target = Math.floor(position/10);
    let mainMemory = this.state.inMain.slice();
    let outMemory = this.state.outMain.slice();
    let strategy = this.state.strategy.slice();
    let cur_position =mainMemory.indexOf(target);
    if(cur_position===-1){
      this.setState({
        currentFault:this.state.currentFault+1,
      });
     if(mainMemory.length===4){
       let index = outMemory.indexOf(target);
       outMemory.splice(index,1);
       if(strategy==='FIFO'){
         mainMemory.push(target);
         let first = mainMemory.shift();
         outMemory.push(first);
       }else {
         mainMemory.unshift(target);
         let last = mainMemory.pop();
         outMemory.push(last);
       }

     }else {
       let index = outMemory.indexOf(target);
       outMemory.splice(index,1);
       if(strategy==='FIFO'){
         mainMemory.push(target);
       }else {
         mainMemory.unshift(target);
       }
     }
    }else {
      if(strategy==='LRU'){
        mainMemory.splice(cur_position,1);
        mainMemory.unshift(target);
      }
    }
    this.setState({
      inMain:mainMemory,
      outMain:outMemory,
    })
  };
  onReset = (e) =>{
    this.setState({
      currentFault:0,
    })
  };
  handleStrategy = (e)=>{
    this.setState({
      strategy:e.target.value,
    })
  };
  render(){
    return (
        <div className='container'>
          <div className='columns'>
            <div className='column is-one-third'>
              <h2>MEMORY SIZE IS 4!</h2>
              <h2>EACH BLOCK CONTAINS 10 INSTRUCTIONS</h2>
              <Strategy handleStrategy={this.handleStrategy}/>
              <Control
                  onLengthChange={this.onLengthChange}
                  onLengthSubmit={this.onLengthSubmit}
                  Length={this.state.maxPosition}
                  OnNextOne={this.OnNextOne}
                  OnPreviousOne={this.OnPreviousOne}
                  onChange={this.onChange}
                  position={this.state.currentPosition}
                  max_position={this.state.maxPosition}
                  current_fault={this.state.currentFault}
                  onReset={this.onReset}
                  PositionSubmit = {this.onPositionSubmit}
              />
            </div>
            <div className='column'>
              <MemoDisplay memo={this.state.inMain} target={Math.floor(this.state.currentPosition/10)}/>
            </div>
            <div className='column'>
              <MemoDisplay memo={this.state.outMain} target={Math.floor(this.state.currentPosition/10)}/>
            </div>
          </div>
        </div>
    );
  }
}
class Strategy extends React.Component{
  render() {
    return(
        <div className="select">
          <select onChange={this.props.handleStrategy}>
            <option value='FIFO'>FIFO</option>
            <option value='LRU'>LRU</option>
          </select>
        </div>
    );
  }
}
function MemoDisplay(props) {
  let colomn = Math.floor(props.memo.length/5);
  const memo = props.memo.slice();
  let showCase = [];
  let count = 0;
  for(let i =0;i<colomn;i++){
    let tempt = [];
    for(let j = 0;j<5;j++){
      tempt.push(memo[count]);
      count++;
    }
    showCase.push(tempt);
  }
  let remain = [];
  while (count < memo.length) {
    remain.push(memo[count]);
    count++;
  }
  if(remain.length!==0){
    showCase.push(remain);
  }
  const style = {
    'width': '70px',
  };
  return(
      <div>
        {
          showCase.map((value,index)=>{
            return(
                <div key={index.toString()}>
                  {
                    value.map((value,index)=>{
                      let color = null;
                      if(value!==props.target){
                        color = value;
                      }else {
                        color = <font color={'red'}>{value}</font>
                      }
                      return(
                          <button key={index} style={style}>{color}</button>
                      );
                    })
                  }
                </div>
            );
          })
        }
      </div>
  );
}
function Control(props){
  return(
      <div>
        <LengthSetting onLengthChange={props.onLengthChange} onLengthSubmit={props.onLengthSubmit} length={props.Length}/>
        <ExecPosition OnNextOne={props.OnNextOne} OnPreviousOne={props.OnPreviousOne} onChange={props.onChange} position={props.position} max_position={props.max_position}
                      handleSubmit={props.PositionSubmit}/>
        <CurrentFault current_fault={props.current_fault}/>
        <ResetFaultNumber onReset={props.onReset}/>
      </div>
  );
}

function LengthSetting({onLengthChange,onLengthSubmit,length}) {
  if(length<0){
    throw new Error('Length is negative');
  }
  return(
      <div>
        <label>How many blocks do you like?</label>
        <form onSubmit={onLengthSubmit}>
          <input type={'number'} value={length} onChange={onLengthChange}/>
          <input type={'submit'} value={'OK'}/>
        </form>
      </div>
  );
}

function ExecPosition({max_position,onChange,OnNextOne,OnPreviousOne,position,handleSubmit}) {
  if(position>max_position*10){
    throw new Error('Position exceed!');
  }
  return(
      <div>
        <label>Current position of the program.</label>
        <form onSubmit={handleSubmit}>
          <input type={'number'} value={position} onChange={onChange}/>
          <button type={'submit'}>OK</button>
        </form>
        <div>
          <button onClick={OnNextOne}>Next instruction</button>
          <button onClick={OnPreviousOne}>Previous instruction</button>
        </div>
      </div>
  );
}
function ResetFaultNumber({onReset}){
  return(
      <div>
        <label>Reset the <b>fault number</b></label>
        <button onClick={onReset}>Reset</button>
      </div>
  );
}
function CurrentFault({current_fault}) {
  return (
      <label>{current_fault}</label>
  );
}

export default MainWindow;



