import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EcoReport } from '../models/eco-report';

@Injectable({
  providedIn: 'root'
})
export class EcoAnalyzerService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // 1. CREATE (Analyze Code)
  analyzeCode(rawJavaCode: string, language: string, executionScale: number): Observable<EcoReport> {
    return this.http.post<EcoReport>(this.apiUrl + '/analyze', {
      rawJavaCode: rawJavaCode,
      language: language,
      executionScale: executionScale
    });
  }

  // 2. READ ALL
  getAllReports(): Observable<EcoReport[]> {
    return this.http.get<EcoReport[]>(this.apiUrl + '/reports');
  }

  // 3. UPDATE
  updateReport(id: number, reportData: Partial<EcoReport>): Observable<EcoReport> {
    return this.http.put<EcoReport>(this.apiUrl + '/reports/' + id, reportData);
  }

  // 4. DELETE
  deleteReport(id: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/reports/' + id);
  }
}