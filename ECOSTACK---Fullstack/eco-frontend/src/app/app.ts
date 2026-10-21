import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent {
  selectedLanguage: string = 'JAVA';
  executionScale: number = 1000000;
  rawCode: string = '';
  report: any = null;
  loading: boolean = false;

  constructor(private http: HttpClient) {}

  // 1. CREATE OPERATION (Execute Audit)
  onExecuteAudit() {
    if (!this.rawCode.trim()) return;

    this.loading = true;
    const payload = {
      rawJavaCode: this.rawCode,
      language: this.selectedLanguage,
      executionScale: Number(this.executionScale)
    };

    this.http.post('http://localhost:8080/api/analyze', payload)
      .subscribe({
        next: (response: any) => {
          this.report = response;
          this.loading = false;
        },
        error: (err: any) => {
          console.error('Audit execution error:', err);
          this.loading = false;
        }
      });
  }

  // 2. UPDATE OPERATION (Update Suggestion / Directives)
  onUpdateReport() {
    if (!this.report || !this.report.id) return;

    const updatedData = {
      suggestion: this.report.suggestion + '\n[Updated: Verified against Green AST standard]',
      optimizedCode: this.report.optimizedCode
    };

    this.http.put('http://localhost:8080/api/reports/' + this.report.id, updatedData)
      .subscribe({
        next: (response: any) => {
          this.report = response;
          alert('Report updated successfully in MySQL database!');
        },
        error: (err: any) => {
          console.error('Error updating report:', err);
        }
      });
  }

  // 3. DELETE OPERATION (Delete Record from DB and reset UI)
  onDeleteReport() {
    if (!confirm('Are you sure you want to delete this analysis report?')) return;

    if (this.report && this.report.id) {
      this.http.delete('http://localhost:8080/api/reports/' + this.report.id)
        .subscribe({
          next: () => {
            alert('Report deleted successfully from MySQL database!');
            this.report = null;
            this.rawCode = '';
          },
          error: (err: any) => {
            console.error('Error deleting report:', err);
          }
        });
    } else {
      this.report = null;
      this.rawCode = '';
    }
  }
}