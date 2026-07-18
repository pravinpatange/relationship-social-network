import { Component, signal } from "@angular/core";
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from "@angular/forms";
import { RouterLink, Router } from "@angular/router";
import { CommonModule } from "@angular/common";
import { AuthService } from "../../../core/services/auth.service";

@Component({
  selector: "app-register",
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: "./register.component.html",
  styleUrl: "./register.component.css"
})
export class RegisterComponent {
  form: FormGroup;
  loading = signal(false);
  error = signal("");
  showPass = signal(false);
  step = signal(1);

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      displayName: ["", [Validators.required, Validators.minLength(2)]],
      username: ["", [Validators.required, Validators.minLength(3), Validators.maxLength(50), Validators.pattern(/^[a-zA-Z0-9_]+$/)]],
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.minLength(8)]],
    });
  }

  nextStep() {
    const c = ["displayName","username"];
    c.forEach(f => this.form.get(f)?.markAsTouched());
    if (c.every(f => this.form.get(f)?.valid)) this.step.set(2);
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true); this.error.set("");
    this.auth.register(this.form.value).subscribe({
      next: () => this.router.navigate(["/feed"]),
      error: (e) => { this.error.set(e.error?.message || "Registration failed"); this.loading.set(false); }
    });
  }
}