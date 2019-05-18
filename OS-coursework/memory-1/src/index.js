import React from 'react';
import ReactDOM from 'react-dom';
import './../node_modules/bulma/css/bulma.css';



class Status extends React.Component{
    render() {
        const {status} = this.props;
        return(
            <div>
                <label>The allocation is</label>
                <div>{status}</div>
            </div>
        );
    }
}
class DemandInput extends React.Component{
    render() {
        return(
            <div>
                <div>Please enter your demand in the following form</div>
                <div>1 200</div>
                <div>This means you want 200 units to be allocated to process 1</div>
                <form onSubmit={this.props.onSubmit}>
                    <label>
                        ProcessNumber:
                        <input type="number" value={this.props.processNumber} onChange={this.props.onNumberChange} />
                    </label>
                    <label>
                        ProcessDemand:
                        <input type='number' value={this.props.processDemand} onChange={this.props.onDemandChange}/>
                    </label>
                    <input type="submit" value="提交请求" />
                </form>
            </div>
        );
    }

}
class Size extends React.Component{
    render() {
        return (
            <form onSubmit={this.props.onSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.props.size} onChange={this.props.onChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}
class Strategy extends React.Component{
    render() {
        return(
            <div className="select">
                <select onChange={this.props.handleStrategy}>
                    <option value='first'>首次适应</option>
                    <option value='best'>最佳适应</option>
                </select>
            </div>
        );
    }
}
class Controller extends React.Component{
    render() {
        return(
            <div>
                <Strategy handleStrategy = {this.props.handleStrategy}/>
                <Size onChange={this.props.handleSize}
                      size = {this.props.size}
                      onSubmit = {this.props.submitSize}
                />
                <DemandInput onNumberChange={this.props.handleNumberChange}
                             processDemand = {this.props.processDemand}
                             processNumber = {this.props.processNumber}
                             onDemandChange = {this.props.handleDemandChange}
                             onSubmit = {this.props.submitDemand}
                />
                <Status status={this.props.status}/>
            </div>
        );
    }
}
class Displayed extends React.Component{
    render() {
        let showCase = [];
        const style = {
            'width': '40px',
        };
        let groupNumber = Math.floor(this.props.memo.length / 20);
        let count = 0;
        for(let i = 0;i<groupNumber;i++){
            let tempt = [];
            for(let j = 0;j<20;j++){
                tempt.push({unit:this.props.memo[count],id:count});
                count++;
            }
            showCase.push({menocase:tempt,id:i.toString()});
        }
        let remain = [];
        for(;count<this.props.memo.length;count++){
            remain.push({unit:this.props.memo[count],id:count});
        }
        if(remain.length!==0) {
            showCase.push({menocase:remain,id:groupNumber});
        }
        return(
            <div>
                {
                    showCase.map((item)=>{
                        return (
                            <div key={item.id} className='buttons'>
                                {
                                    item.menocase.map((unit)=>{
                                        let to_show = null;
                                        if(unit.unit===-1){
                                            to_show = unit.unit;
                                        }else {
                                            to_show = <b>{unit.unit}</b>
                                        }
                                        return (
                                            <button style={style} key={unit.id}>{to_show}</button>
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

}
class MemoryAllocator extends React.Component{
    constructor(props) {
        super(props);
        this.state ={
          strategy : 'first',
          size : 400,
          current_processNumber :0,
          current_processDemand: 0,
          demand : [],
          result:'',
          memo : Array(400).fill(-1),
        };
    }
    handleStrategy = (e)=>{
        console.log(e.target.value);
        this.setState(
            {strategy:e.target.value}
        )
    };
    handleSize = (e)=>{
        let number = parseInt(e.target.value);
        if(isNaN(number)){
            number = 0;
        }
        this.setState({
            size:number
        });
        console.log(number);
    };
    handleNumber = (e)=>{
        console.log(e.target.value);
        this.setState({
            current_processNumber:e.target.value
        })

    };
    submitSize = (e)=>{
        e.preventDefault();
        //alert('Size is submitted!');
        console.log('size is '+this.state.size);
        this.setState({
            memo:Array(this.state.size).fill(-1),
            demand:[]
        })
    };
    submitDemand = (e)=>{
        e.preventDefault();
        //alert('demand submitted!');
        let new_demand = this.state.demand.slice();
        const thisNumber = parseInt(this.state.current_processNumber);
        const thisDemand = parseInt(this.state.current_processDemand);
        if(isNaN(thisNumber)||isNaN(thisDemand)){
            this.setState({
                result:'Invalid input!'
            });
            return;
        }
        let current_allocation = this.state.memo.slice();

        let if_already = false;
        for(let i = 0;i<new_demand.length;i++){
            if(new_demand[i].processNumber === thisNumber){
                new_demand[i].processDemand += thisDemand;
                new_demand[i].satisfied = 1;
                if(new_demand[i].processDemand<=0){
                    for (let t = 0; t < current_allocation.length;t++){
                        if(current_allocation[t]===new_demand[i].processNumber){
                            current_allocation[t]=-1;
                        }
                    }
                    this.setState({
                        result:'Delete process'+ new_demand[i].processNumber,
                    });
                    new_demand.splice(i,1);
                }
                this.setState({
                    demand : new_demand
                });
                if_already = true;
            }
        }
        if(!if_already && thisDemand<=0){
            return;
        }
        // 0 nothing
        // 1 modified
        // 2 OK
        if(!if_already){
            new_demand.push({processNumber:thisNumber,processDemand:thisDemand,satisfied:0});
            this.setState({
                demand : new_demand
            });
        }
        console.log(new_demand);
        //TODO 如果不够大，直接删除整个程序。
        for(let k = 0;k<new_demand.length;k++){
            let demand = new_demand[k];
            console.log('current demand'+demand);
            if(demand.satisfied===0){
                if(this.state.strategy==='first'){
                    for(let i = 0;i<current_allocation.length;i++){
                        if(current_allocation[i]===-1){
                            let start = i;
                            while (i < current_allocation.length && current_allocation[start]===-1 && start-i + 1!== demand.processDemand){
                                start++;
                            }
                            if(start-i+1===demand.processDemand){
                                for (let j = i; j <= start; j++) {
                                    current_allocation[j]=demand.processNumber;
                                }
                                demand.satisfied = 2;
                                this.setState({
                                    result:'Distribution of # '+demand.processNumber+' is OK'
                                });
                                break;
                            }
                        }
                    }
                    if(demand.satisfied!==2) {
                        this.setState({
                            result: 'Distribution of # ' + demand.processNumber + ' failed!'
                        })
                        new_demand.splice(k,1);
                    }
                }
                else {
                    let best_size = Infinity;
                    let best_begin = -1;
                    for(let i = 0;i<current_allocation.length;i++){
                        if(current_allocation[i]===-1){
                            let start = i;
                            while (start<current_allocation.length && current_allocation[start] === -1) {
                                start++;
                            }
                            let size = start - i + 1;
                            if(size>=demand.processDemand && size<best_size){
                                best_size = size;
                                best_begin = i;
                            }
                            i = start;
                        }
                    }
                    console.log('best'+best_begin+'  '+best_size);
                    if(best_size!==Infinity){
                        for(let i = 0;i<demand.processDemand;i++){
                            current_allocation[best_begin++]=demand.processNumber;
                        }
                        this.setState({
                            result:'Distribution of # '+demand.processNumber+' is OK'
                        });
                        demand.satisfied = 2;
                    }else {
                        this.setState({
                            result: 'Distribution of # ' + demand.processNumber + ' failed!'
                        })
                        new_demand.splice(k,1);
                    }
                }
            }else if(demand.satisfied===1){
                let i = 0;
                for(;i<current_allocation.length;i++){
                    if(current_allocation[i]===demand.processNumber){
                        break;
                    }
                }
                let start = i;
                while (i < current_allocation.length && (current_allocation[start]===-1||current_allocation[start]===demand.processNumber)
                        && start-i + 1!== demand.processDemand){
                    start++;
                }
                if(start-i+1===demand.processDemand){
                    for (let j = i; j <= start; j++) {
                        current_allocation[j]=demand.processNumber;
                    }
                    demand.satisfied = 2;
                    if(start+1<current_allocation.length && current_allocation[++start]===demand.processNumber){
                        while (start<current_allocation.length && current_allocation[start]===demand.processNumber){
                            current_allocation[start]=-1;
                            start++;
                        }
                    }
                    this.setState({
                        result:'Distribution of # '+demand.processNumber+' is OK'
                    });
                }
                if(demand.satisfied!==2){
                    while (current_allocation[i]===demand.processNumber){
                        current_allocation[i]=-1;
                        i++;
                    }
                    demand.satisfied = 0;
                    k--;
                }

            }
        }
        console.log(current_allocation);
        this.setState({
            memo:current_allocation,
        })

    };
    handelNumberChange = (e)=>{
        console.log(e.target.value);
        this.setState({
            current_processNumber:e.target.value
        });
    };
    handleDemandChange = (e)=>{
        console.log(e.target.value);
        this.setState({
            current_processDemand:e.target.value
        });
    };


    render() {
        return(
            <div className='container'>
                <div className='columns'>
                    <div className='column is-one-quarter'>
                        <Controller
                            handleStrategy = {this.handleStrategy}
                            handleSize = {this.handleSize}
                            submitSize = {this.submitSize}
                            handleNumberChange = {this.handelNumberChange}
                            handleDemandChange = {this.handleDemandChange}
                            submitDemand = {this.submitDemand}
                            demand = {this.state.current_demand}
                            status = {this.state.result}
                            size = {this.state.size}
                        />
                    </div>
                    <div className='column'>
                        <Displayed
                            memo = {this.state.memo}
                        />
                    </div>
                </div>
            </div>
        );
    }
}


// ========================================

ReactDOM.render(
    <MemoryAllocator />,
    document.getElementById('root')
);
if(module.hot){
    module.hot.accept();
}


