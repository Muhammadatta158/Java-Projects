import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Employee } from './employee';
import { EmployeeService } from './employee.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule, NgFor } from '@angular/common';
import {FormsModule, NgForm} from '@angular/forms'

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] 
})
export class AppComponent implements OnInit {

  title = 'empolyeemanagerapp';
  @ViewChild('addEmployeeModal') addEmployeeModal: ElementRef | undefined;
  @ViewChild('editEmployeeModal') editEmployeeModal: ElementRef | undefined;
  @ViewChild('deleteEmployeeModal') deleteEmployeeModal: ElementRef | undefined;


  public employees: Employee[] = []; 

  public selectedEmployee: Employee | null = null;
  public editEmployee: Employee | null = null;
  public deleteEmployee: Employee | null = null;
 
  constructor(private employeeService: EmployeeService){}

  public closeModal(): void {
    const modal = document.querySelector('.modal.show');
    if (modal) {
      //document.body.focus();
      (window as any).bootstrap.Modal.getInstance(modal)?.hide();
    }
  }
  

  ngOnInit(): void {
      this.getEmployees();
  }


  public getEmployees(): void { 
    this.employeeService.getEmployees().subscribe(
      (response: Employee[]) => {
        this.employees = response; 
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onAddEmployee(addForm: NgForm) : void{

    (document.activeElement as HTMLElement)?.blur();
    document.getElementById("add-employee-form")!.click();
    this.employeeService.addEmployees(addForm.value).subscribe(
      (response: Employee) => {
        console.log(response);
        this.getEmployees();
        addForm.resetForm();
        this.closeModal();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onUpdateEmloyee(employee: Employee): void {
    this.employeeService.updateEmployees(employee).subscribe(
      (response: Employee) => {
        console.log(response);
        this.getEmployees();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onDeleteEmloyee(employeeId: number): void {
    this.employeeService.deleteEmployees(employeeId).subscribe(
      (response: void) => {
        console.log(response);
        this.getEmployees();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }


  public onOpenModal(employee: Employee | null, mode: string):void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    this.selectedEmployee = employee;
    if (mode === 'add') {
      this.showModal(this.addEmployeeModal);
    } else if (mode === 'edit') {
      this.editEmployee = employee;
      this.showModal(this.editEmployeeModal);
    } else if (mode === 'delete') {
      this.showModal(this.deleteEmployeeModal);
    }
  }

  private showModal(modalRef: ElementRef | undefined): void {
    const modal = modalRef?.nativeElement;
    const bootstrapModal = new (window as any).bootstrap.Modal(modal);
    bootstrapModal.show();
  }

  public onSubmit(): void {
    console.log('Submit button clicked!');
  }
}



